package com.spring.taskmanger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.spring.taskmanger.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.spring.taskmanger.dao.UserRepository;
import com.spring.taskmanger.exception.ResourceNotFoundException;
import com.spring.taskmanger.model.User;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

	CustomUserDetails customUserDetails =
			(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

	public ResponseEntity<User> getUser( ) throws ResourceNotFoundException {
		Optional<User> userOptional = userRepository.findById(customUserDetails.getId());
		logger.info("User retrived Succefully");
		return ResponseEntity.ok().body(userOptional.get());
	}

	public ResponseEntity<?> deleteUser(Long id) {

		return userRepository.findById(id).map(user -> {
			userRepository.delete(user);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("UserID " + id + " not found"));

	}

	public ResponseEntity<User> updateUser(User user1) throws ResourceNotFoundException {

		User user = userRepository.findById(customUserDetails.getId()).get();

		user.setName(user1.getName());
		user.setEmail(user1.getEmail());
		user.setPassword(user1.getEmail());

		return ResponseEntity.ok(userRepository.save(user));

	}


}
