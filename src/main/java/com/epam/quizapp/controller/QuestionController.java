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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.quizapp.dto.QuestionDTO;
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.exceptions.QuizNotFoundException;
import com.epam.quizapp.service.QuestionService;

@RestController
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired
	QuestionService questionService;
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<List<QuestionDTO>> getAllQuestions() throws NoQuestionsFoundException {
		return new ResponseEntity<>(questionService.getQuestions(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable("id") int questionId) {
		return new ResponseEntity<>(questionService.getQuestionById(questionId), HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<QuestionDTO> addQuestion(@RequestBody QuestionDTO questionDTO)
			throws QuizNotFoundException {
		return new ResponseEntity<>(questionService.addQuestion(questionDTO), HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteQuestionById(@PathVariable("id") int questionId) {
		questionService.deleteQuestion(questionId);

		return ResponseEntity.ok("Question With Id " + questionId + "is Successfully Deleted");

	}
	
	@PutMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<QuestionDTO> updateQuestion( @RequestBody QuestionDTO questionDTO) {
		return new ResponseEntity<>(questionService.editQuestion(questionDTO), HttpStatus.OK);
	}
	
}
