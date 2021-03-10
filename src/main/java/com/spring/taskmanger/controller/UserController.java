package com.spring.taskmanger.controller;

import java.util.List;

import javax.validation.Valid;

import com.spring.taskmanger.dao.TokenRepository;
import com.spring.taskmanger.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@Autowired
	TokenRepository tokenRepository;

	@GetMapping("/users")
	// ResponseEntity represents the whole HTTP response: status code, headers, and
	// body, Used in RestTemplate as well @Controller methods.
	public ResponseEntity<User> getUser() {
		return userService.getUser();
	}

	@PutMapping("/users")
	public ResponseEntity<User> UpdateUser(@Valid @RequestBody User user) {
		return userService.updateUser(user);

	}


}
