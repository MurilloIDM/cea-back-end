package com.cea.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cea.exception.response.HttpExceptionResponse;

@ControllerAdvice
public class HttpExceptionAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<HttpExceptionResponse> HttpClientErrorException(HttpClientErrorException ex) {
		HttpExceptionResponse response = new HttpExceptionResponse();
		
		response.setMessage(ex.getStatusText());
		response.setStatus(ex.getStatusCode().value());
		
		return new ResponseEntity<>(response, ex.getStatusCode());
	}
	
}
