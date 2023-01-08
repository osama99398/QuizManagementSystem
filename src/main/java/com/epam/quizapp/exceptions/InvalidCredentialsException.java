package com.epam.quizapp.exceptions;

public class InvalidCredentialsException extends RuntimeException{
	
	
	private static final long serialVersionUID = 1L;
	final String message;

	public InvalidCredentialsException(String message) {
		super(message);
		this.message = message;
	}
}
