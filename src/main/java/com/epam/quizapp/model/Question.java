package com.epam.quizapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int questionId;
	private String questionDescription;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private Integer answer;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quizId")
	private Quiz quiz;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Question() {
		this.questionDescription = "";
		this.option1 = "";
		this.option2 = "";
		this.option3 = "";
		this.option4 = "";
		this.answer = 0;
	}

	public Question(String questionDescription, String option1, String option2, String option3, String option4,
			Integer answer) {
		this.questionDescription = questionDescription;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.answer = answer;
	}

	public String getQuestionDescription() {
		return questionDescription;
	}

	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(Integer answer) {
		this.answer = answer;
	}
}
