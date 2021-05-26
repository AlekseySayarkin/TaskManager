package by.bsuir.spp.resolver.mutation;

import by.bsuir.spp.model.Message;
import by.bsuir.spp.model.Task;
import by.bsuir.spp.model.TaskDto;
import by.bsuir.spp.model.TaskInput;
import by.bsuir.spp.service.TaskService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TaskMutation implements GraphQLMutationResolver {

    private final TaskService taskService;

    @Autowired
    public TaskMutation(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task createTask(TaskInput taskInput) {
        var task = new Task();
        task.setTask(taskInput.getTask());
        DateTimeFormatter Parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(taskInput.getEndDate(), Parser);

        task.setEndDate(date.atStartOfDay(ZoneId.systemDefault()));

        return taskService.save(1, task, null);
    }

    public Message deleteTask(Integer id) {
        taskService.deleteById(id);
        var message = new Message();
        message.setMessage("Ok");
        return message;
    }

    public Task updateTask(TaskInput taskInput, Integer id) {
        Task task = taskService.findById(id);
        task.setTask(taskInput.getTask());
        DateTimeFormatter Parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(taskInput.getEndDate(), Parser);

        task.setEndDate(date.atStartOfDay(ZoneId.systemDefault()));
        return taskService.update(task, null);
    }
}
