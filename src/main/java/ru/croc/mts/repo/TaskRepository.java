package ru.croc.mts.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.croc.mts.model.Task;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, String> {
    List<Task> findAllByOrderByCreatedAtDesc();

    @Query(value = "SELECT t.id, t.path, t.priority, coalesce(e.avg_cpu,t.cpu) cpu, coalesce(e.avg_memory,t.memory) memory, t.exec_time, t.created_at, t.status " +
            "FROM task t LEFT JOIN executable e on t.path=e.path where priority = ?1 and status = ?2 and coalesce(e.avg_cpu,t.cpu) < ?3 and coalesce(e.avg_memory,t.memory) < ?4 " +
            "order by (coalesce(e.avg_cpu,t.cpu)+coalesce(e.avg_memory,t.memory)) desc for update of t", nativeQuery = true)
    List<Task> getMatchingTasks(int priority, int status, double cpu, double memory);

    @Query("SELECT max(t.priority) FROM Task t")
    Integer getMaxPriority();


}
