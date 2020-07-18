package ru.croc.mts.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.croc.mts.model.Status;
import ru.croc.mts.model.Task;
import ru.croc.mts.repo.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Component("TaskSelector")
public class TaskSelector {

    private Logger logger = LogManager.getLogger(TaskSelector.class);

    @Autowired
    private TaskRepository taskRepo;

    @Transactional
    public List<Task> getTasksForExecution(double freeCpu, double freeMem){
        logger.info("Looking for tasks matching available cpu and memory: " + freeCpu + " / " + freeMem);
        Integer maxPriority = taskRepo.getMaxPriority();
        logger.info("Max priority found: " + maxPriority);
        if (maxPriority!=null) {
            List<Task> allTasks = taskRepo.getMatchingTasks(maxPriority, Status.WAITING.ordinal(), freeCpu, freeMem);
            // TODO : For really long-running tasks it might make sense to add Knapsack solver to maximize resources utilization
            if (allTasks.size()>0) {
                List<Task> result = new ArrayList<>();
                double sumCpu = 0, sumMemory = 0;
                for (Task task : allTasks) {
                    sumCpu += task.getCpu();
                    sumMemory += task.getMemory();
                    if (sumCpu < freeCpu && sumMemory < freeMem) {
                        result.add(task);
                        task.setStatus(Status.IN_PROGRESS);
                        taskRepo.save(task);
                    } else {
                        break;
                    }
                }
                return result;
            }

        }
        return null;
    }

}
