package by.bsuir.spp.web.controller;

import by.bsuir.spp.model.Task;
import by.bsuir.spp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @MessageMapping("/get/user/tasks")
    @SendTo("/ws/greetings")
    public ResponseEntity<List<Task>> getTasks(String status) {
        var userId = 1;
        return ResponseEntity.ok().body(taskService.findByUserId(userId, 1, 100, "asc", "task", status.replace("\"", "")));
    }
    
    @MessageMapping("/post/user/tasks/{id}")
    @SendTo("/ws/greetings")
    public ResponseEntity<Task> saveTask(@RequestPart Task task) {
        var userId = 1;
        return ResponseEntity.ok().body(taskService.save(userId, task, null));
    }
    
    @MessageMapping("/get/user/tasks/file")
    @SendTo("/ws/greetings")
    public ResponseEntity<byte[]> downloadFile(Integer id) {
        var task = taskService.findById(id);
        return ResponseEntity.ok().body(task.getFile().getData());
    }
    
    @MessageMapping("/put/user/tasks/{id}")
    @SendTo("/ws/greetings")
    public ResponseEntity<Task> updateTask(@RequestPart("task") Task task) {
        return ResponseEntity.ok().body(taskService.update(task, null));
    }
    
    @MessageMapping("/delete/user/tasks/{id}")
    @SendTo("/ws/greetings")
    public HttpStatus deleteTask(Integer id) {
        taskService.deleteById(id);
        return HttpStatus.OK;
    }
}
