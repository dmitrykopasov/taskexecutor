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
    private Map<String, ProcessHolder> processMap = new HashMap<>();

    private OperatingSystemMXBean opSysBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    @Autowired
    private TaskSelector taskSelector;

    @Autowired
    private StatStore statStore;

    @PostConstruct
    public void init(){
        worker = new Thread(this);
        worker.start();
    }

    public void run(){
        /*
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {

        }*/
        while (isActive) {

            if (processMap.size()>0) {
                Map<Long, PsStat> allStats = getSystemLoadByProcess();
                for (Map.Entry<String, ProcessHolder> entry : processMap.entrySet()) {
                    ProcessHolder holder = entry.getValue();
                    Process process = holder.getProcess();
                    if (process.isAlive()) {
                        PsStat stat = allStats.get(holder.getPid());
                        if (stat==null) {
                            logger.warn("Process stats not found for [" + holder.getPid() + "]");
                        } else {
                            holder.getSummaryStat().merge(stat);
                        }
                    } else {
                        processMap.remove(entry.getKey());
                        statStore.storeStatsAndRemoveTask(holder, entry.getKey());
                    }
                }
            } else {
                logger.info("Process map is empty: no any running process");
            }


            List<Task> list = taskSelector.getTasksForExecution(
                    1 - opSysBean.getSystemCpuLoad(),
                    opSysBean.getFreePhysicalMemorySize()/opSysBean.getTotalPhysicalMemorySize() );
            if (list==null) {
                logger.info("No task selected for execution");
            } else {
                logger.info(list.size() + " tasks selected for execution");
                for (Task task : list) {
                    try {
                        Process process = Runtime.getRuntime().exec(task.getPath());
                        ProcessHolder holder = new ProcessHolder(task.getPath(), process);
                        processMap.put(task.getId(), holder);
                    } catch (Exception e) {
                        logger.warn("Failed to launch task: " + task.getPath());
                        throw new RuntimeException(e);
                    }
                }
            }

            Thread.yield();


            /*
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {

            }*/
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

    private Map<Long, PsStat> getSystemLoadByProcess(){
        Map<Long, PsStat> result = new HashMap<>();
        try {
            Process process = Runtime.getRuntime().exec("ps -o pid,%cpu,%mem -e --no-headers");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            while(reader.ready()) {
                String line = reader.readLine();
                String[] parts = line.trim().split("\\s+");
                if (parts.length==3) {
                    PsStat stat = new PsStat(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                    result.put(Long.parseLong(parts[0]), stat);
                } else {
                    logger.warn("Unparsed PS output line: " + line);
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
