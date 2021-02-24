package com.spring.taskmanger.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


//GlobalException Handling 
@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler  {
	//this class is handle the ResourceNotFoundException if there is another exception 
	// i have to creat a new class of the exception and create new method here for that exception 
	
	@ExceptionHandler(ResourceNotFoundException.class)// This annotation is for handling a specific exception and send the custom response to the client 
	public ResponseEntity<?> handleResourceNotFoundException
	        (ResourceNotFoundException resourceNotFoundException, WebRequest request) {
		
		ErrorDetials errorDetials = new ErrorDetials(new Date(), resourceNotFoundException.getMessage(), request.getDescription(false));
		return new ResponseEntity(errorDetials , HttpStatus.NOT_FOUND); 
		//  Create an error response object and return it with a specific Http Status.
	}
	
	@ExceptionHandler(Exception.class)// This annotation is for handling a specific exception and send the custom response to the client 
	//You can further enhance CustomizedResponseEntityExceptionHandler to handle all other exceptions.

	public ResponseEntity<?> handleGlobalException
	        (Exception exception, WebRequest request) {
		
		ErrorDetials errorDetials = new ErrorDetials(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity(errorDetials , HttpStatus.INTERNAL_SERVER_ERROR); 
		
	}
	

}
