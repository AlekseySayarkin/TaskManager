package by.bsuir.spp.web.controller;

import by.bsuir.spp.model.Task;
import by.bsuir.spp.security.UserDetailsImpl;
import by.bsuir.spp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user/tasks")
@CrossOrigin(origins="http://localhost:4200")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestParam Integer page, @RequestParam Integer size,
                               @RequestParam String sortType, @RequestParam String sortBy, @RequestParam String status) {
        var details = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = details.getId();
        return ResponseEntity.ok().body(taskService.findByUserId(userId, page, size, sortType, sortBy, status));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Task> saveTask(@RequestPart Task task, MultipartFile file) {
        var details = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = details.getId();
        return ResponseEntity.ok().body(taskService.save(userId, task, file));
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        var task = taskService.findById(id);
        var resource = new ByteArrayResource(task.getFile().getData());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header("Content-Disposition", "attachment; filename=\"" + task.getFile().getName() + "\"")
                .body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@RequestPart Task task, MultipartFile file) {
        return ResponseEntity.ok().body(taskService.update(task, file));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTask(@PathVariable Integer id) {
        taskService.deleteById(id);
        return HttpStatus.OK;
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        var details = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = details.getId();
        return ResponseEntity.ok().body(taskService.countTaskByUserId(userId));
    }
}
