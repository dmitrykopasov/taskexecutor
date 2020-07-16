package ru.croc.mts.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.croc.mts.model.Task;
import ru.croc.mts.repo.TaskRepository;

import java.util.List;

@Component("TaskSelector")
public class TaskSelector {

    private Logger logger = LogManager.getLogger(TaskSelector.class);

    @Autowired
    private TaskRepository taskRepo;

    @Transactional
    public List<Task> getTasksForExecution(double freeCpu, double freeMem){
        Integer maxPriority = taskRepo.getMaxPriority();
        logger.info("Max priority found: " + maxPriority);
        return null;
    }

}
