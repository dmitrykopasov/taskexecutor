package ru.croc.mts.repo;

import org.springframework.data.repository.CrudRepository;
import ru.croc.mts.model.Executable;

public interface ExecutableRepository extends CrudRepository<Executable, String> {
}
