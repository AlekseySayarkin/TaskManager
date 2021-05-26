package by.bsuir.spp.resolver;

import by.bsuir.spp.model.Task;
import by.bsuir.spp.service.TaskService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskResolver implements GraphQLQueryResolver {

    private final TaskService taskService;

    @Autowired
    public TaskResolver(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task task(int id) {
        log.info(String.format("Retrieving task with id: %s", id));
        return taskService.findById(id);
    }

    public boolean deleteTask(int id) {
        taskService.deleteById(id);
        return true;
    }
}
