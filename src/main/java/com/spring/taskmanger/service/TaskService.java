package com.spring.taskmanger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.spring.taskmanger.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

	public ResponseEntity<Page<Task>> getAllTasks(Pageable pageable) throws ResourceNotFoundException {

		Page<Task> tasks = taskRepository.findByUserId(getCurrentUser().getId(), pageable);

		if (tasks.isEmpty()) {
			logger.error("There is an Error of getting user of id " + getCurrentUser().getId());
			throw new ResourceNotFoundException("No Tasks found for this user");
		}

		logger.info("getting all the task of user id  " + getCurrentUser().getId() + "is successed");

		return ResponseEntity.ok().body(tasks);

		// Page object has the number of total pages, number of the current page and
		// well as whether current page is first page or last page.

	}

	public ResponseEntity<?> addTask(Task task) throws ResourceNotFoundException {

		task.setUser(getCurrentUser());
		logger.info("New Task was saved successfully to User with Id" + getCurrentUser().getId());
		Task task1 = taskRepository.save(task);
		return ResponseEntity.ok().body("New Task was saved successfully" + task1);

	}

	public ResponseEntity<Task> getTask(Long taskId) {

		Optional<Task> optional = taskRepository.findByIdAndUserId(taskId, getCurrentUser().getId());
		if (!(optional.isPresent())) {
			logger.error("Error while retriving tasks ");
			throw new ResourceNotFoundException("UserId " + getCurrentUser().getId() + "with TaskId " + taskId + "not found");
		}
		return ResponseEntity.ok().body(optional.get());
	}

	public ResponseEntity<Task> updateTask(Task task1, Long taskId) throws ResourceNotFoundException {

		return taskRepository.findByIdAndUserId(taskId, getCurrentUser().getId()).map(task -> {
			task.setDescription(task1.getDescription());
			task.setCompleted(task1.isCompleted());
			return ResponseEntity.ok().body(taskRepository.save(task));
		}).orElseThrow(() -> new ResourceNotFoundException("taskId " + taskId +"with current User"+ getCurrentUser().getId() + "not found"));

	}

	public ResponseEntity<?> deleteTask(Long taskId) throws ResourceNotFoundException {

		return taskRepository.findByIdAndUserId(taskId, getCurrentUser().getId()).map(task -> {
			taskRepository.delete(task);
			return ResponseEntity.ok().body("Task Succefully Deleted !");
		}).orElseThrow(
				() -> new ResourceNotFoundException("Task not found with taaskId " + taskId + " and userId " + getCurrentUser().getId()));
	}

	private User getCurrentUser() {

		CustomUserDetails customUserDetails =
				(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return customUserDetails.getUser();
	}


}
