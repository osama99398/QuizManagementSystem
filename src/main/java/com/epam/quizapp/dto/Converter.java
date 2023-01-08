package com.epam.quizapp.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.quizapp.model.Question;
import com.epam.quizapp.model.Quiz;
import com.epam.quizapp.model.User;



@Component
public class Converter {
	
	@Autowired
	private ModelMapper modelMapper;

	public Question convertDTOtoEntity(QuestionDTO questionDTO) {

		return modelMapper.map(questionDTO, Question.class);
	}

	public QuestionDTO convertEntitytoDTO(Question question) {

		return modelMapper.map(question, QuestionDTO.class);
	}

	public Quiz convertDTOtoEntity(QuizDTO quizDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(quizDTO, Quiz.class);
	}

	public QuizDTO convertEntitytoDTO(Quiz quiz) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(quiz, QuizDTO.class);
	}
	
	public User convertDTOtoEntity(UserDTO userDTO) {
		return modelMapper.map(userDTO, User.class);
	}

	public UserDTO convertEntitytoDTO(User user) {

		return modelMapper.map(user, UserDTO.class);
	}
}
