package by.bsuir.spp.web.controller;

import by.bsuir.spp.model.Task;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.ZonedDateTime;


@Controller
@CrossOrigin(origins={"http://localhost:4200", "http://0.0.0.0:4200"})
public class WebSocketController {
	
	@MessageMapping("/get/user/tasks/kek")
	@SendTo("/ws/greetings")
	public Task greeting(String message) throws Exception {
		System.out.println(message);
		var task = new Task();
		task.setTask("kek");
		task.setEndDate(ZonedDateTime.now());
		return task;
	}
}
