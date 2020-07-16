package ru.croc.mts.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.croc.mts.model.Task;
import ru.croc.mts.repo.TaskRepository;

import java.util.List;

@Component
public class TaskSelector {

    @Autowired
    private TaskRepository taskRepo;

    @Transactional
    public List<Task> getTasksForExecution(){

        return null;
    }

}
