package com.epam.quizapp.exceptions;

public class NoQuestionsFoundException extends Exception {
	
	
	private static final long serialVersionUID = 1L;
	final String message;

	public NoQuestionsFoundException(String message) {
		super(message);
		this.message = message;
	}
}
