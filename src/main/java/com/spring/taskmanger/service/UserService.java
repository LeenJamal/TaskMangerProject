package com.spring.taskmanger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.taskmanger.dao.UserRepository;
import com.spring.taskmanger.exception.ResourceNotFoundException;
import com.spring.taskmanger.model.User;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;

	}

	public User addUser(User user) {
		User user1 = userRepository.save(user);
		logger.info("New User Added Succefully");
		return user1;

	}

	public ResponseEntity<User> getUser(Long id) throws ResourceNotFoundException {
		Optional<User> userOptional = userRepository.findById(id);
		if (!(userOptional.isPresent())) {
			logger.error("Resource Not Found Exception While retriving the User");
			throw new ResourceNotFoundException("User not found with id " + id);
		}
		logger.info("User retrived Succefully");
		return ResponseEntity.ok().body(userOptional.get());
	}

	public ResponseEntity<?> deleteUser(Long id) {

		return userRepository.findById(id).map(user -> {
			userRepository.delete(user);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("UserID " + id + " not found"));

	}

	public ResponseEntity<User> updateUser(Long id, User user1) throws ResourceNotFoundException {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

		user.setName(user1.getName());
		user.setEmail(user1.getEmail());
		user.setPassword(user1.getEmail());

		// final User updatedEmployee = userRepository.save(user);
		return ResponseEntity.ok(userRepository.save(user));

	}

}
