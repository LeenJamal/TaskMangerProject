package com.spring.taskmanger.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.spring.taskmanger.model.User;
import com.spring.taskmanger.service.UserService;

@RestController
public class UserController {
	@Autowired
	UserService userService;

	@GetMapping("/users")
	public List<User> getAllUsers() {

		return userService.getAllUsers();
	}

	@GetMapping("/users/{id}")
	// ResponseEntity represents the whole HTTP response: status code, headers, and
	// body, Used in RestTemplate as well @Controller methods.
	public ResponseEntity<User> getUser(@PathVariable Long id) {

		return userService.getUser(id);
	}

	@PostMapping("/users")
	public User addUser(@Valid @RequestBody User user) {
		return userService.addUser(user);

	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		return userService.deleteUser(id);

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> UpdateUser(@Valid @RequestBody User user, @PathVariable Long id) {
		return userService.updateUser(id, user);

	}

}
