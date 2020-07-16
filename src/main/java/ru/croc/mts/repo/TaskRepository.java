package ru.croc.mts.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.croc.mts.model.Task;

public interface TaskRepository extends CrudRepository<Task, String> {
    Page<Task> findAllByOrderByCreatedAtDesc(Pageable page);

    @Query("SELECT max(t.priority) FROM Task t")
    Integer getMaxPriority();
}
