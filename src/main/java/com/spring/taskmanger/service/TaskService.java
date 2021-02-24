package com.spring.taskmanger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.taskmanger.controller.TaskController;
import com.spring.taskmanger.dao.TaskRepository;
import com.spring.taskmanger.dao.UserRepository;
import com.spring.taskmanger.exception.ResourceNotFoundException;
import com.spring.taskmanger.model.Task;
import com.spring.taskmanger.model.User;

@Service

public class TaskService {

	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	UserRepository userRepository;

	public ResponseEntity<Page<Task>> getAllTasks(Long userId, Pageable pageable) throws ResourceNotFoundException {

		Page<Task> tasks = taskRepository.findByUserId(userId, pageable);

		if (tasks.isEmpty()) {
			logger.error("There is an Error of getting user of id " + userId);
			throw new ResourceNotFoundException("UserId " + userId + " not Exist");
		}

		logger.info("getting all the task of user id  " + userId + "is successed");

		return ResponseEntity.ok().body(tasks);

		// return taskRepository.findByUserId(userId, pageable);

		// Page object has the number of total pages, number of the current page and
		// well as whether current page is first page or last page.

	}

	public ResponseEntity<Task> addTask(Task task, Long userId) throws ResourceNotFoundException {

		if (!userRepository.existsById(userId)) {
			logger.error("There is an Error of adding a new task,The given ID is not found ");

			throw new ResourceNotFoundException("UserId " + userId + " not found");
		}

		User user = userRepository.findById(userId).get();
		task.setUser(user);
		logger.info("New Task was saved successfully to User with Id" + userId);
		return ResponseEntity.ok().body(taskRepository.save(task));

		/*
		 * userRepository.findById(userId).map(user -> { task.setUser(user); return
		 * taskRepository.save(task); }).orElseThrow(() -> new
		 * ResourceNotFoundException("UserId " + userId + " not found"));
		 */
	}

	public ResponseEntity<Task> getTask(Long userId, Long taskId) {

		Optional<Task> optional = taskRepository.findByIdAndUserId(taskId, userId);
		if (!(optional.isPresent())) {
			logger.error("Error while retriving tasks ");

			throw new ResourceNotFoundException("UserId " + userId + "with TaskId " + taskId + "not found");

		}

		return ResponseEntity.ok().body(optional.get());

	}

	public ResponseEntity<Task> updateTask(Task task1, Long userId, Long taskId) throws ResourceNotFoundException {

		if (!userRepository.existsById(userId)) {
			logger.error("Error while Updating tasks ");
			throw new ResourceNotFoundException("UserId " + userId + " not found");
		}

		return taskRepository.findById(taskId).map(task -> {
			task.setDescription(task1.getDescription());
			task.setCompleted(task1.isCompleted());
			task.setUser(task1.getUser());
			return ResponseEntity.ok().body(taskRepository.save(task));
		}).orElseThrow(() -> new ResourceNotFoundException("taskId " + taskId + "not found"));

	}

	public ResponseEntity<?> deleteTask(Long userId, Long taskId) throws ResourceNotFoundException {

		return taskRepository.findByIdAndUserId(taskId, userId).map(task -> {
			taskRepository.delete(task);
			return ResponseEntity.ok().build();
		}).orElseThrow(
				() -> new ResourceNotFoundException("Task not found with taaskId " + taskId + " and userId " + userId));
	}

}
