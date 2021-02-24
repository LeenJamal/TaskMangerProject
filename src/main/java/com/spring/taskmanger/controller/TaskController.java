package com.spring.taskmanger.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.taskmanger.model.Task;
import com.spring.taskmanger.model.User;
import com.spring.taskmanger.service.TaskService;
import com.spring.taskmanger.service.UserService;

@RestController

public class TaskController {

	@Autowired
	TaskService taskService;

	@GetMapping("/users/{userId}/tasks")
	public ResponseEntity<Page<Task>> getAllTasks(@PathVariable Long userId, Pageable pageable) {

		return taskService.getAllTasks(userId, pageable);
	}

	@GetMapping("/users/{userId}/tasks/{taskId}")
	public ResponseEntity<Task> getTask(@PathVariable Long userId, @PathVariable Long taskId) {
		return taskService.getTask(userId, taskId);
	}

	@PostMapping("/users/{userId}/tasks")
	public ResponseEntity<Task> addTask(@Valid @RequestBody Task task, @PathVariable Long userId) {

		return taskService.addTask(task, userId);
	}

	@PutMapping("/users/{userId}/tasks/{taskid}")
	public ResponseEntity<Task> updateTask(@Valid @RequestBody Task task, @PathVariable Long userId,
			@PathVariable Long taskid) {

		return taskService.updateTask(task, userId, taskid);
	}

	@DeleteMapping("/users/{userId}/tasks/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable Long userId, @PathVariable Long taskId) {

		return taskService.deleteTask(userId, taskId);
	}

}
