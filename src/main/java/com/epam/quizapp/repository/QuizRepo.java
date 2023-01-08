package com.epam.quizapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.quizapp.model.Quiz;

public interface QuizRepo extends JpaRepository<Quiz, Integer> {

	public Optional<Quiz> findByQuizName(String quizName);

	public int deleteById(int quizId);
}
