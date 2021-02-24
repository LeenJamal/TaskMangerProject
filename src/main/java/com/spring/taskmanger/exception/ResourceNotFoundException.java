package com.spring.taskmanger.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // to tell Spring Boot to respond with a 404 status when this exception is thrown
public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}