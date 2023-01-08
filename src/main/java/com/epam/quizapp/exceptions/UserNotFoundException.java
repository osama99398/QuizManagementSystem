package com.epam.quizapp.exceptions;

public class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	final String message;

	public  UserNotFoundException(String message) {
		super(message);
		this.message = message;
	}
}
