package com.epam.quizapp.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.quizapp.dto.QuestionDTO;
import com.epam.quizapp.dto.QuizDTO;
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.service.QuizService;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
	
	@Autowired
	QuizService quizService;
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<List<QuizDTO>> getAllQuizes() {
		return new ResponseEntity<>(quizService.getQuizes(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<List<QuestionDTO>> getQuizQuestionsById(@PathVariable("id") int quizId)
			throws NoQuestionsFoundException {
		return new ResponseEntity<>(quizService.getQuestionsByQuizId(quizId), HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<QuizDTO> addQuiz(@RequestBody QuizDTO quizDTO) {
		return new ResponseEntity<>(quizService.addQuiz(quizDTO), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteQuiz(@PathVariable("id") int quizId) {
		quizService.deleteQuiz(quizId);
		return new ResponseEntity<>("Quiz has deleted Successfully", HttpStatus.OK);
	}
}
