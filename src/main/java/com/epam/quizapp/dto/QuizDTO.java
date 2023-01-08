package com.epam.quizapp.dto;

import java.util.List;

public class QuizDTO {
	
	private int quizId;
	
	private String quizName;
	private List<QuestionDTO> questions;

	public QuizDTO(String quizName) {
		super();
		this.quizName = quizName;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public QuizDTO() {

	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}
}
