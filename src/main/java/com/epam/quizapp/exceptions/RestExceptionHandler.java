package com.epam.quizapp.exceptions;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = { UserNotFoundException.class, QuestionNotFoundException.class,
			QuizNotFoundException.class, NoQuestionsFoundException.class, InvalidCredentialsException.class })
	public ResponseEntity<ExceptionResponse> handleExceptions(Exception exception, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setError(exception.getMessage());
		exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.name());
		exceptionResponse.setTimeStamp(LocalDate.now().toString());
		exceptionResponse.setPath(request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
}
