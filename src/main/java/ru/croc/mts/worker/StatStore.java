package ru.croc.mts.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.croc.mts.model.Executable;
import ru.croc.mts.model.ExecutionLog;
import ru.croc.mts.repo.ExecutableRepository;
import ru.croc.mts.repo.ExecutionLogRepository;
import ru.croc.mts.repo.TaskRepository;
import ru.croc.mts.util.CommonUtil;

import java.util.Date;
import java.util.List;

@Component("StatStore")
public class StatStore {

    @Autowired
    private ExecutionLogRepository logRepo;

    @Autowired
    private ExecutableRepository executableRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Transactional
    public void storeStatsAndRemoveTask(ProcessHolder holder){
        Executable executable = new Executable();
        executable.setPath(holder.getExecPath());
        ExecutionLog newLog = new ExecutionLog();
        newLog.setExecutable(executable);
        if (holder.getSummaryStat()==null) {
            newLog.setCpu(0.);
            newLog.setMemory(0.);
        } else {
            newLog.setCpu(holder.getSummaryStat().getAvgCpu());
            newLog.setMemory(holder.getSummaryStat().getMemory());
        }
        newLog.setExecTime(holder.getDuration());
        newLog.setCreatedAt(new Date());
        newLog.setId(CommonUtil.createUuid());
        logRepo.save(newLog);
        List<ExecutionLog> logList = logRepo.findAllByExecutable(executable);
        int size = logList.size();
        if (size>0) {
            double sumCpu = 0, sumMemory = 0;
            long sumDuration = 0;
            for (ExecutionLog log : logList) {
                sumCpu += log.getCpu();
                sumMemory += log.getMemory();
                sumDuration += log.getExecTime();
            }
            executable.setAvgCpu(sumCpu / size);
            executable.setAvgMemory(sumMemory / size);
            executable.setAvgDuration(Math.round(sumDuration / size));
        }
        executableRepo.save(executable);
        taskRepo.deleteById(holder.getTaskId());
    }
}
