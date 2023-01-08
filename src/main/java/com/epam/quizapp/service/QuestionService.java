package com.epam.quizapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.quizapp.repository.QuestionRepo;
import com.epam.quizapp.repository.QuizRepo;
import com.epam.quizapp.dto.Converter;
import com.epam.quizapp.dto.QuestionDTO;
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.exceptions.QuestionNotFoundException;
import com.epam.quizapp.exceptions.QuizNotFoundException;
import com.epam.quizapp.model.Question;
import com.epam.quizapp.model.Quiz;

@Service
public class QuestionService {
	
	@Autowired
	QuestionRepo questionRepo;
	
	@Autowired
	QuizRepo quizRepo;
	
	@Autowired
	Converter converter;
	
	public QuestionDTO addQuestion(QuestionDTO questionDTO) throws QuizNotFoundException {

		Question question = converter.convertDTOtoEntity(questionDTO);

		Quiz quiz = quizRepo.findByQuizName(questionDTO.getQuizName())
				.orElseThrow(() -> new QuizNotFoundException("Quiz Not Found"));

		question.setQuiz(quiz);

		return converter.convertEntitytoDTO(questionRepo.save(question));

	}
	
	public QuestionDTO getQuestionById(int questionId) throws QuestionNotFoundException {

		Question question = questionRepo.findById(questionId)
				.orElseThrow(() -> new QuestionNotFoundException("Question with questionId does not Exist"));

		return converter.convertEntitytoDTO(question);
	}
	
	public List<QuestionDTO> getQuestions() throws NoQuestionsFoundException {
		List<Question> questions = questionRepo.findAll();
		if (questions.isEmpty()) {
			throw new NoQuestionsFoundException("No Questions Exist");
		}
		return questionRepo.findAll().stream().map(q -> converter.convertEntitytoDTO(q)).toList();
	}
	
	public QuestionDTO editQuestion(QuestionDTO question) {
		Question existedQuestion = questionRepo.findById(question.getQuestionId())
				.orElseThrow(() -> new QuestionNotFoundException("Question not found with given Exception"));
		existedQuestion.setQuestionDescription(question.getQuestionDescription());
		existedQuestion.setOption1(question.getOption1());
		existedQuestion.setOption2(question.getOption2());
		existedQuestion.setOption3(question.getOption3());
		existedQuestion.setOption4(question.getOption4());
		existedQuestion.setAnswer(question.getAnswer());
		return converter.convertEntitytoDTO(questionRepo.save(existedQuestion));
	}
	
	@Transactional
	public void deleteQuestion(int questionId) {
		questionRepo.findById(questionId)
				.orElseThrow(() -> new QuestionNotFoundException("Question with questionId does not Exist"));
		questionRepo.deletebyQuestionId(questionId);
	}
	
}
