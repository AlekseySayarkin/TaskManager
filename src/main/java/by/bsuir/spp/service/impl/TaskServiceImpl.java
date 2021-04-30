package by.bsuir.spp.service.impl;

import by.bsuir.spp.exception.NotFoundException;
import by.bsuir.spp.exception.TaskManagerException;
import by.bsuir.spp.model.File;
import by.bsuir.spp.model.Status;
import by.bsuir.spp.model.Task;
import by.bsuir.spp.repository.FileRepository;
import by.bsuir.spp.repository.TaskRepository;
import by.bsuir.spp.service.TaskService;
import by.bsuir.spp.service.UserService;
import by.bsuir.spp.service.util.TaskValidator;
import by.bsuir.spp.service.util.UserValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final FileRepository fileRepository;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, FileRepository fileRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.fileRepository = fileRepository;
        this.userService = userService;
    }

    @Override
    public Task findById(Integer id) throws TaskManagerException {
        TaskValidator.validateTaskId(id);
        try {
            return taskRepository.findById(id).orElseThrow(() -> {
                log.error("Failed to find task by id: " + id);
                return new NotFoundException("Failed to find task by id: " + id);
            });
        } catch (DataAccessException e) {
            log.error("Failed to find task");
            throw new NotFoundException("Failed to find task");
        }
    }

    @Override
    public List<Task> findByUserId(Integer userId, Integer page, Integer size,
                                   String sortType, String sortBy, String status) {
        UserValidator.validateUserId(userId);
        try {
            return filter(
                taskRepository.findByUserId(userId, PageRequest.of(--page, size, getSort(sortType, sortBy))), status
            );
        } catch (DataAccessException e) {
            log.error("Failed to find tasks by user id");
            throw new NotFoundException("Failed to find task by user id");
        }
    }

    private List<Task> filter(List<Task> task, String status) {
        return switch (status) {
            case "Active" -> task.stream()
                    .filter(t -> t.getStatus().getStatus().equals(Status.StatusType.ACTIVE))
                    .collect(Collectors.toList());
            case "Finished" -> task.stream()
                    .filter(t -> t.getStatus().getStatus().equals(Status.StatusType.FINISHED))
                    .collect(Collectors.toList());
            case "Postponed" -> task.stream()
                    .filter(t -> t.getStatus().getStatus().equals(Status.StatusType.POSTPONED))
                    .collect(Collectors.toList());
            default -> task;
        };
    }

    private Sort getSort(String sortType, String sortBy) {
        return sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }

    @Override
    @Transactional(rollbackFor = TaskManagerException.class)
    public Task save(Integer userId, Task task, MultipartFile file) throws TaskManagerException {
        UserValidator.validateUserId(userId);
        TaskValidator.validateTask(task);
        try {
            if (file != null)
               task.setFile( fileRepository.save(saveFile(file)));

            task.setStatus(new Status(1, Status.StatusType.ACTIVE));
            task.setUser(userService.findById(userId));

            return taskRepository.save(task);
        } catch (DataAccessException | IOException e) {
            log.error("Failed to save task");
            throw new NotFoundException("Failed to save task");
        }
    }

    @Override
    @Transactional(rollbackFor = TaskManagerException.class)
    public void deleteById(Integer id) throws TaskManagerException {
        TaskValidator.validateTaskId(id);
        try {
            taskRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Failed to delete task by id: " + id);
            throw new TaskManagerException("Failed to delete task by id: " + id);
        }
    }

    @Override
    @Transactional(rollbackFor = TaskManagerException.class)
    public Task update(Task task, MultipartFile file) throws TaskManagerException {
        TaskValidator.validateTask(task);
        try {
            if (file != null) {
                task.setFile(saveFile(file));
            }
            return updateExistingTask(task);
        } catch (DataAccessException | IOException e) {
            log.error("Failed to update task");
            throw new TaskManagerException("Failed to update task");
        }
    }

    private File saveFile(MultipartFile file) throws IOException {
        var fileToSave = new File();
        fileToSave.setName(file.getOriginalFilename());
        fileToSave.setData(file.getBytes());

        return fileToSave;
    }

    private Task updateExistingTask(Task existingTask) throws TaskManagerException {
        var task = taskRepository.findById(existingTask.getId()).orElseThrow(() -> {
            log.trace("Failed to find task by id: " + existingTask.getId());
            return new TaskManagerException("Failed to fined task by id: " + existingTask.getId());
        });

        if (existingTask.getTask() != null)
            task.setTask(existingTask.getTask());
        if (existingTask.getStatus() != null)
            task.setStatus(getStatusFromTask(existingTask));
        if (existingTask.getEndDate() != null)
            task.setEndDate(existingTask.getEndDate());
        if (existingTask.getFile() != null && existingTask.getFile().getData().length != 0)
            task.setFile(fileRepository.save(existingTask.getFile()));
        else if (task.getFile() != null && existingTask.getFile() == null)
            deleteFile(task);

        return taskRepository.save(task);
    }

    private Status getStatusFromTask(Task task) {
        return switch (task.getStatus().getStatus()) {
            case ACTIVE -> new Status(1, Status.StatusType.ACTIVE);
            case FINISHED -> new Status(2, Status.StatusType.FINISHED);
            case POSTPONED -> new Status(3, Status.StatusType.POSTPONED);
        };
    }

    private void deleteFile(Task task) {
        fileRepository.delete(task.getFile());
        task.setFile(null);
    }

    @Override
    public long countTaskByUserId(Integer userId) throws TaskManagerException {
        UserValidator.validateUserId(userId);
        try {
            return taskRepository.countTaskByUserId(userId);
        } catch (DataAccessException e) {
            log.error("Failed to get count of tasks");
            throw new TaskManagerException("Failed to get count of tasks");
        }
    }
}
