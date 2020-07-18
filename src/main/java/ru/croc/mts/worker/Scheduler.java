package ru.croc.mts.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sun.management.OperatingSystemMXBean;
import ru.croc.mts.model.Task;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@DependsOn({"TaskSelector","StatStore"})
public class Scheduler implements DisposableBean, Runnable {

    private Logger logger = LogManager.getLogger(Scheduler.class);

    private Thread worker;
    private volatile boolean isActive = true;

    /**
     * Plain old HashMap is good enough, since only one worker thread accesses it.
     */
    private Map<Long, ProcessHolder> processMap = new HashMap<>();

    private OperatingSystemMXBean opSysBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private int coreNum;

    @Autowired
    private TaskSelector taskSelector;

    @Autowired
    private StatStore statStore;

    @PostConstruct
    public void init(){
        coreNum = opSysBean.getAvailableProcessors();
        worker = new Thread(this);
        worker.start();
    }

    public void run(){

        while (isActive) {

            if (processMap.size()>0) {
                Map<Long, PsStat> allStats = getSystemLoadByProcess();
                for (Map.Entry<Long, ProcessHolder> entry : processMap.entrySet()) {
                    ProcessHolder holder = entry.getValue();
                    Process process = holder.getProcess();
                    if (process.isAlive()) {
                        PsStat stat = allStats.get(holder.getPid());
                        if (stat==null) {
                            logger.warn("Process stats not found for [" + holder.getPid() + "]");
                        } else {
                            if (holder.getSummaryStat()==null) {
                                holder.setSummaryStat(stat);
                            } else {
                                holder.getSummaryStat().merge(stat);
                            }
                        }
                    } else {
                        processMap.remove(entry.getKey());
                        statStore.storeStatsAndRemoveTask(holder);
                    }
                }
            } else {
                logger.info("Process map is empty: no any running process");
            }


            List<Task> list = taskSelector.getTasksForExecution(
                    (1 - opSysBean.getSystemCpuLoad())*100.0,
                    (double)opSysBean.getFreePhysicalMemorySize()*100.0/opSysBean.getTotalPhysicalMemorySize()  );
            if (list==null) {
                logger.info("No task selected for execution");
            } else {
                logger.info(list.size() + " tasks selected for execution");
                for (Task task : list) {
                    try {
                        Process process = Runtime.getRuntime().exec(task.getPath());
                        ProcessHolder holder = new ProcessHolder(task.getPath(), task.getId(), process);
                        processMap.put(holder.getPid(), holder);
                    } catch (Exception e) {
                        logger.warn("Failed to launch task: " + task.getPath());
                        throw new RuntimeException(e);
                    }
                }
            }

            // Hey, that's a background thread and it needs a delay, to avoid too much pressure on database
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }

        }
    }

    @Override
    public void destroy(){
        isActive = false;
        try {
            worker.join();
        } catch (InterruptedException e) {

        }
    }

    /**
     * Merge process stats recursively
     * @param psTree
     * @param data
     * @param pid
     * @param result
     */
    private void mergePsTreeData(Map<Long, List<Long>> psTree, Map<Long, PsStat> data, Long pid, PsStat result){
        List<Long> children = psTree.get(pid);
        if (children!=null) {
            for (Long child : children) {
                PsStat stat = data.get(child);
                if (stat!=null) {
                    result.merge(stat);
                }
                mergePsTreeData(psTree, data, child, result);
            }
        }
    }

    /**
     * System utilization data from PS, grouped by spawned process ID
     * @return
     */
    private Map<Long, PsStat> getSystemLoadByProcess(){
        Map<Long, PsStat> result = new HashMap<>();
        Map<Long, List<Long>> psTree = new HashMap<>();
        Map<Long, PsStat> data = new HashMap<>();
        try {
            Process process = Runtime.getRuntime().exec("ps -o pid,ppid,%cpu,%mem -e --no-headers");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            while(reader.ready()) {
                String line = reader.readLine();
                String[] parts = line.trim().split("\\s+");
                if (parts.length==4) {
                    long pid = Long.parseLong(parts[0]);
                    long parentPid = Long.parseLong(parts[1]);

                    // Populate process tree
                    List<Long> children = psTree.get(parentPid);
                    if (children==null) {
                        children=new ArrayList<Long>();
                        psTree.put(parentPid, children);
                    }
                    children.add(pid);

                    // Populate data store
                    PsStat stat = new PsStat(Double.parseDouble(parts[2])/coreNum, Double.parseDouble(parts[3]));
                    data.put(pid, stat);

                } else {
                    logger.warn("Unparsed PS output line: " + line);
                }
            }

            for (Long key : processMap.keySet()) {
                PsStat stat = data.get(key);
                if (stat!=null) {
                    mergePsTreeData(psTree, data, key, stat);
                    result.put(key, stat);
                }
            }

        } catch (Exception e) {
            logger.error("Failed to get utilization stats from OS.", e);
            throw new RuntimeException(e);
        }
        logger.info("PS stats completed with " + result.size() + " entries");
        return result;
    }



}
