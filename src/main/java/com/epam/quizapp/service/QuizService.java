package com.epam.quizapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.epam.quizapp.dto.Converter;
import com.epam.quizapp.dto.QuestionDTO;
import com.epam.quizapp.dto.QuizDTO;
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.exceptions.QuizNotFoundException;
import com.epam.quizapp.model.Question;
import com.epam.quizapp.model.Quiz;
import com.epam.quizapp.repository.QuizRepo;

@Service
public class QuizService {
	
	@Autowired
	QuizRepo quizRepo;
	
	@Autowired
	Converter converter;
	
	public QuizDTO addQuiz(QuizDTO quizDTO) {

		Quiz quiz = converter.convertDTOtoEntity(quizDTO);
		if (quiz.getQuestions() != null) {
			quiz.getQuestions().stream().forEach(q -> q.setQuiz(quiz));
		}
		return converter.convertEntitytoDTO(quizRepo.save(quiz));
	}
	

	public List<String> getTopics() {

		return quizRepo.findAll().stream().map(Quiz::getQuizName).toList();
	}
	
	public List<QuizDTO> getQuizes() {

		return quizRepo.findAll().stream().map(q -> converter.convertEntitytoDTO(q)).toList();
	}
	
	public List<QuestionDTO> getQuestionsByQuizId(int quizId) throws NoQuestionsFoundException {

		Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new QuizNotFoundException("Quiz Not found"));

		List<Question> questions = quiz.getQuestions();
		if (questions.isEmpty()) {
			throw new NoQuestionsFoundException("No Questions found ");
		}

		return questions.stream().map(q -> converter.convertEntitytoDTO(q)).toList();
	}
	
	public void deleteQuiz(int quizId) {
		quizRepo.delete(quizRepo.findById(quizId).orElseThrow(() -> new QuizNotFoundException("Quiz Not found")));

	}

}
