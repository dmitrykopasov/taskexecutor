package ru.croc.mts.repo;

import org.springframework.data.repository.CrudRepository;
import ru.croc.mts.model.ExecutionLog;

public interface ExecutionLogRepository extends CrudRepository<ExecutionLog, String> {
}
