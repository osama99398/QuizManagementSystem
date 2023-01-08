package com.epam.quizapp.exceptions;

public class QuestionNotFoundException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;
	final String message;

	public QuestionNotFoundException(String message) {
		super(message);
		this.message = message;
	}
}
