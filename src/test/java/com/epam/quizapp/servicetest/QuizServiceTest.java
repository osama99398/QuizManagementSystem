package com.epam.quizapp.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.quizapp.dto.Converter;
import com.epam.quizapp.dto.QuizDTO;
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.exceptions.QuizNotFoundException;
import com.epam.quizapp.model.Question;
import com.epam.quizapp.model.Quiz;
import com.epam.quizapp.repository.QuizRepo;
import com.epam.quizapp.service.QuizService;



@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

	@Mock
	private QuizRepo quizRepo;
	
	@InjectMocks
	private QuizService quizService;
	
	
	@Mock
	private Converter converter;
	
	QuizDTO quizDTO1=new QuizDTO("Java");
	QuizDTO quizDTO2=new QuizDTO("Python");
	
	Quiz quiz1=new Quiz("Java");
	Quiz quiz2=new Quiz("Python");
	
	Question question1=new Question("Q1", "1", "2", "3","4",2);
	Question question2=new Question("Q1", "1", "2", "3","4",2);
	List<Question> questions=Arrays.asList(question1,question2);

	@Test
	void test_addQuiz()
	{
		when(quizRepo.save(any(Quiz.class))).thenReturn(quiz1);
		when(converter.convertDTOtoEntity(any(QuizDTO.class))).thenReturn(quiz1);
		when(converter.convertEntitytoDTO(any(Quiz.class))).thenReturn(quizDTO1);
		assertNotNull(quizService.addQuiz(quizDTO1));
	}
	@Test
	void test_addQuiz_questionExist()
	{
		quiz1.setQuestions(questions);
		when(quizRepo.save(any(Quiz.class))).thenReturn(quiz1);
		when(converter.convertDTOtoEntity(any(QuizDTO.class))).thenReturn(quiz1);
		when(converter.convertEntitytoDTO(any(Quiz.class))).thenReturn(quizDTO1);
		assertNotNull(quizService.addQuiz(quizDTO1));
	}
	
	
	
	@Test
	void test_getTopics() {
		
		List<Quiz> quizs=Arrays.asList(new Quiz("java"),new Quiz("C++"));
	    when(quizRepo.findAll()).thenReturn(quizs);
	    assertEquals(2,quizService.getTopics().size());
	}
	
	@Test
	void test_getAllQuizes()
	{ 
		List<Quiz> quizs=Arrays.asList(new Quiz("java"),new Quiz("C++"));
	    when(quizRepo.findAll()).thenReturn(quizs);
	    assertEquals(2,quizService.getQuizes().size());
		
	}
	
	@Test
	void test_deleteQuiz_IdNotExist()
	{
		when(quizRepo.findById(anyInt())).thenReturn(Optional.empty());
		assertThrows(QuizNotFoundException.class, ()->quizService.deleteQuiz(1));
		
	}
	
	@Test
	void test_deleteQuiz_IdExist()
	{
		when(quizRepo.findById(anyInt())).thenReturn(Optional.of(quiz1));
		quizService.deleteQuiz(1);
		verify(quizRepo,times(1)).delete(any(Quiz.class));
	}
	
	@Test
	void test_getQuestions_IdExist_questionExist() throws NoQuestionsFoundException
	{

		Question question1=new Question("Q1", "1", "2", "3","4",2);
		Question question2=new Question("Q1", "1", "2", "3","4",2);
		List<Question> questions=Arrays.asList(question1,question2);
		quiz1.setQuestions(questions);
		when(quizRepo.findById(anyInt())).thenReturn(Optional.of(quiz1));
		assertEquals(2, quizService.getQuestionsByQuizId(2).size());
		
	}
	@Test
	void test_getQuestions_IdExist_questionNotExist() throws NoQuestionsFoundException
	{
		quiz1.setQuestions(Arrays.asList());
		when(quizRepo.findById(anyInt())).thenReturn(Optional.of(quiz1));
		assertThrows(NoQuestionsFoundException.class,()->quizService.getQuestionsByQuizId(2) );
	}

	@Test
	void test_getQuestions_IdExist() {
		when(quizRepo.findById(anyInt())).thenReturn(Optional.empty());
		assertThrows(QuizNotFoundException.class,()->quizService.getQuestionsByQuizId(2) );
	}


}
