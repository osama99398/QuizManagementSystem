package com.epam.quizapp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Quiz {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quizId;

	@OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
	private List<Question> questions;
	private String quizName;

	public Quiz() {

	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public Quiz(String quizName, List<Question> questions) {
		this.setQuestions(questions);
		setQuizName(quizName);

	}

	public Quiz(String quizName) {
		this.setQuestions(questions);
		setQuizName(quizName);

	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Question> getQuestions() {
		return questions;
	}
}
