package com.lcl.scs.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lcl.scs.util.logging.LoggingUtilities;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFound(ResourceNotFoundException ex) {
		LoggingUtilities.generateErrorLog(ex.getMessage());
		/*
		 * ExceptionResponse response = new ExceptionResponse();
		 * response.setErrorCode("NOT_FOUND");
		 * response.setErrorMessage(ex.getMessage());
		 */
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> badRequestException(BadRequestException ex) {
		LoggingUtilities.generateErrorLog(ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exception(Exception ex) {
		LoggingUtilities.generateErrorLog(ex.getMessage());
		/*
		 * ExceptionResponse response = new ExceptionResponse();
		 * response.setErrorCode("SERVER_ERROR");
		 * response.setErrorMessage(ex.getMessage());
		 */
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), headers, status);
	}

}
