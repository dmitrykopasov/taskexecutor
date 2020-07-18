package ru.croc.mts.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.croc.mts.model.Executable;
import ru.croc.mts.model.Status;
import ru.croc.mts.model.Task;
import ru.croc.mts.repo.ExecutableRepository;
import ru.croc.mts.repo.TaskRepository;
import ru.croc.mts.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Component
@RequestMapping(value = {"/web"})
public class WebController {

    private Logger logger = LogManager.getLogger(WebController.class);

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private ExecutableRepository execRepo;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = "application/json")
    public List<Task> getTasks() {
        List<Task> tasks = taskRepo.findAllByOrderByCreatedAtDesc();
        return tasks;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = "application/json")
    @Transactional
    public ResponseEntity<String> createTask(@RequestBody Task task) {
        String path = task.getExecutable().getPath();
        File file = new File(path);
        if (!file.canExecute()) {
            throw new RuntimeException("Executable does not exist or can not be executed.");
        }
        Optional<Executable> execOpt = execRepo.findById(path);
        if (!execOpt.isPresent()) {
            execRepo.save(task.getExecutable());
        }
        if (task.getId()==null) {
            task.setId(CommonUtil.createUuid());
        }
        if (task.getPriority()==null) {
            task.setPriority(Task.DEFAULT_PRIORITY);
        }
        task.setStatus(Status.WAITING);
        task.setCreatedAt(new Date());
        taskRepo.save(task);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @Transactional
    public ResponseEntity<String> deleteTask(@PathVariable("id") String id) {
        taskRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
}
