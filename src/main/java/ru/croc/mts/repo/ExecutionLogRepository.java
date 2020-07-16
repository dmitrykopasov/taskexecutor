package ru.croc.mts.repo;

import org.springframework.data.repository.CrudRepository;
import ru.croc.mts.model.Executable;
import ru.croc.mts.model.ExecutionLog;

import java.util.List;

public interface ExecutionLogRepository extends CrudRepository<ExecutionLog, String> {
    List<ExecutionLog> findAllByExecutable(Executable executable);
}
