package by.bsuir.spp.repository;

import by.bsuir.spp.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {

    List<Task> findByUserId(Integer userId, Pageable pageable);
    long countTaskByUserId(Integer userId);
}
