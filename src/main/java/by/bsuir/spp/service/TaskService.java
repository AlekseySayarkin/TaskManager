package by.bsuir.spp.service;

import by.bsuir.spp.exception.TaskManagerException;
import by.bsuir.spp.model.Task;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskService {

    Task findById(Integer id) throws TaskManagerException;
    List<Task> findByUserId(Integer userId, Integer page, Integer size, String sortType, String sortBy, String status)
            throws TaskManagerException;
    Task save(Integer userId, Task task, @Nullable MultipartFile file) throws TaskManagerException;
    void deleteById(Integer id) throws TaskManagerException;
    Task update(Task task, MultipartFile file) throws TaskManagerException;
    long countTaskByUserId(Integer userId) throws TaskManagerException;
}
