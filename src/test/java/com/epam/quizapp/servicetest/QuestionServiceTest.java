package com.epam.quizapp.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.quizapp.dto.Converter;
import com.epam.quizapp.dto.QuestionDTO;
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.exceptions.QuestionNotFoundException;
import com.epam.quizapp.exceptions.QuizNotFoundException;
import com.epam.quizapp.model.Question;
import com.epam.quizapp.model.Quiz;
import com.epam.quizapp.repository.QuestionRepo;
import com.epam.quizapp.repository.QuizRepo;
import com.epam.quizapp.service.QuestionService;


@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

	@Mock
	private QuestionRepo questionRepo;
	
	@Mock
	private  QuizRepo quizRepo;
	
	@Mock
	private Converter converter;
	
	@InjectMocks
	private QuestionService questionService; 
	
	
	QuestionDTO questionDTO = new QuestionDTO("Q1","1","2","3","4", 1);
	QuestionDTO questionDTO1 = new QuestionDTO("Q1","1","2","3","4", 1);
	Question question = new Question("Q1","1","2","3","4", 1);
	Question question1 = new Question("Q1","1","2","3","4", 1);

	List<QuestionDTO> questionDTOlist = Arrays.asList(questionDTO, questionDTO1);
	List<Question> questionlist = Arrays.asList(question, question1);
	
	@Test
	void test_addQuestion_quizExists()
	{
		questionDTO = new QuestionDTO();
		questionDTO.setQuestionDescription(question.getQuestionDescription());
		questionDTO.setOption1(question.getOption1());
		questionDTO.setOption2(question.getOption2());
		questionDTO.setOption3(question.getOption3());
		questionDTO.setOption4(question.getOption4());
		questionDTO.setAnswer(question.getAnswer());
		questionDTO.setQuizName("String");
		question.setQuiz(new Quiz("String"));
		Optional<Quiz> quiz=Optional.of(new Quiz("String"));
		when(quizRepo.findByQuizName(anyString())).thenReturn(quiz);
		when(questionRepo.save(any(Question.class))).thenReturn(question);
		when(converter.convertDTOtoEntity(questionDTO)).thenReturn(question);
		when(converter.convertEntitytoDTO(any(Question.class))).thenReturn(questionDTO);
		assertNotNull(questionService.addQuestion(questionDTO));
	}
	@Test
	void test__addQuestion_quizNotExists()
	{
		questionDTO.setQuizName("String");
		question.setQuiz(new Quiz("String"));
		when(converter.convertDTOtoEntity(questionDTO)).thenReturn(question);
		when(quizRepo.findByQuizName(anyString())).thenReturn(Optional.empty());
		assertThrows(QuizNotFoundException.class, ()->questionService.addQuestion(questionDTO));
	}
	@Test
	void test_getQuestionById_IdExists()
	{
		when(questionRepo.findById(anyInt())).thenReturn(Optional.of(question));
		when(converter.convertEntitytoDTO(any(Question.class))).thenReturn(questionDTO);
		assertNotNull(questionService.getQuestionById(2));
		
	}
	@Test
	void test_getQuestionById_IdNotExists()
	{
		when(questionRepo.findById(anyInt())).thenReturn(Optional.empty());
		assertThrows(QuestionNotFoundException.class,()->questionService.getQuestionById(2));
		
	}
	@Test
	void test_getQuestions_questionsExist() throws NoQuestionsFoundException
	{
		when(questionRepo.findAll()).thenReturn(questionlist);
		assertEquals(2, questionService.getQuestions().size());
	}
	
	@Test
	void test_getQuestions_questionsNotExist() throws NoQuestionsFoundException
	{
		List<Question> question=new ArrayList<>();
		when(questionRepo.findAll()).thenReturn(question);
		assertThrows( NoQuestionsFoundException.class,()-> questionService.getQuestions());
	}
	@Test
	void test_editQuestion_IdExist()
	{
		when(questionRepo.findById(anyInt())).thenReturn(Optional.of(question));
		when(questionRepo.save(any(Question.class))).thenReturn(question);
		when(converter.convertEntitytoDTO(any(Question.class))).thenReturn(questionDTO);
		assertNotNull(questionService.editQuestion(questionDTO));		
	}
	@Test
	void test_editQuestion_IdNotExist()
	{
		when(questionRepo.findById(anyInt())).thenReturn(Optional.empty());
		assertThrows(QuestionNotFoundException.class, ()->questionService.editQuestion(questionDTO));
	}
	@Test
	void test_deleteQuestion_IdExist()
	{
		when(questionRepo.findById(anyInt())).thenReturn(Optional.of(question));
		questionService.deleteQuestion(1);
		verify(questionRepo,times(1)).deletebyQuestionId(1);
		
	}
	@Test
	void test_deleteQuestion_IdNotExist()
	{
		when(questionRepo.findById(anyInt())).thenReturn(Optional.empty());
		assertThrows(QuestionNotFoundException.class, ()-> questionService.deleteQuestion(1));
	}

}
