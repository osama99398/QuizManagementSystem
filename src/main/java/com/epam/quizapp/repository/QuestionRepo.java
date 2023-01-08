package com.epam.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.epam.quizapp.model.Question;

public interface QuestionRepo  extends JpaRepository<Question, Integer>{
	@Modifying
	@Query("delete from Question q where q.questionId=?1")
	public int deletebyQuestionId(int questionId);
}
