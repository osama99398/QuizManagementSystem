package com.epam.quizapp.exceptions;

public class QuizNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	final String message;

	public QuizNotFoundException(String message) {
		super(message);
		this.message = message;
	}
}
