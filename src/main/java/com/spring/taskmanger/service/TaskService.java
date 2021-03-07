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

		Page<Task> tasks = taskRepository.findByUserId(getCurrentUserID(), pageable);

		if (tasks.isEmpty()) {
			logger.error("There is an Error of getting user of id " + getCurrentUserID());
			throw new ResourceNotFoundException("No Tasks found for this user");
		}

		logger.info("getting all the task of user id  " + getCurrentUserID() + "is successed");

		return ResponseEntity.ok().body(tasks);

		// return taskRepository.findByUserId(userId, pageable);

		// Page object has the number of total pages, number of the current page and
		// well as whether current page is first page or last page.

	}

	public ResponseEntity<?> addTask(Task task) throws ResourceNotFoundException {

		if (!userRepository.existsById(getCurrentUserID ())) {
			logger.error("There is an Error of adding a new task,The given ID is not found ");

			throw new ResourceNotFoundException("UserId " + getCurrentUserID () + " not found");
		}

		User user = userRepository.findById(getCurrentUserID ()).get();
		task.setUser(user);
		logger.info("New Task was saved successfully to User with Id" + getCurrentUserID ());
		Task task1 = taskRepository.save(task);
		return ResponseEntity.ok().body("New Task was saved successfully" + task1);

		/*
		 * userRepository.findById(userId).map(user -> { task.setUser(user); return
		 * taskRepository.save(task); }).orElseThrow(() -> new
		 * ResourceNotFoundException("UserId " + userId + " not found"));
		 */
	}

	public ResponseEntity<Task> getTask(Long taskId) {

		Optional<Task> optional = taskRepository.findByIdAndUserId(taskId, getCurrentUserID ());
		if (!(optional.isPresent())) {
			logger.error("Error while retriving tasks ");
			throw new ResourceNotFoundException("UserId " + getCurrentUserID () + "with TaskId " + taskId + "not found");
		}
		return ResponseEntity.ok().body(optional.get());
	}

	public ResponseEntity<Task> updateTask(Task task1, Long taskId) throws ResourceNotFoundException {
		/*
		if (!userRepository.existsById(getCurrentUserID ())) {
			logger.error("Error while Updating tasks ");
			throw new ResourceNotFoundException("UserId " + getCurrentUserID () + " not found");
		}
		 */

		return taskRepository.findByIdAndUserId(taskId, getCurrentUserID ()).map(task -> {
			task.setDescription(task1.getDescription());
			task.setCompleted(task1.isCompleted());
			return ResponseEntity.ok().body(taskRepository.save(task));
		}).orElseThrow(() -> new ResourceNotFoundException("taskId " + taskId +"with current User"+ getCurrentUserID () + "not found"));

	}

	public ResponseEntity<?> deleteTask(Long taskId) throws ResourceNotFoundException {

		return taskRepository.findByIdAndUserId(taskId, getCurrentUserID ()).map(task -> {
			taskRepository.delete(task);
			return ResponseEntity.ok().body("Task Succefully Deleted !");
		}).orElseThrow(
				() -> new ResourceNotFoundException("Task not found with taaskId " + taskId + " and userId " + getCurrentUserID ()));
	}

	private Long getCurrentUserID () {
		UserDetails userDetails =
				(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserName = userDetails.getUsername();
		Optional<User> user = userRepository.findByEmail(currentUserName);
		Long userId = user.get().getId();
		return userId;

		/*
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Optional<User> user = userRepository.findByEmail(currentPrincipalName);
		Long userId = user.get().getId();
		 */
	}

}
