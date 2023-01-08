package com.epam.quizapp.controllertest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import com.epam.quizapp.dto.QuestionDTO;
import com.epam.quizapp.dto.QuizDTO;
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.exceptions.QuestionNotFoundException;
import com.epam.quizapp.exceptions.QuizNotFoundException;
import com.epam.quizapp.service.QuizService;
import com.epam.quizapp.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@MockBean
	private QuizService quizService;

	private ObjectMapper objectMapper=new ObjectMapper();
	
	private QuizDTO quizDTO;
	private List<QuestionDTO> questions;
	private String jwt;
	
	@BeforeEach
	void setUpObject()
	{
		QuestionDTO questionDTO=new QuestionDTO();
		questionDTO.setAnswer(1);
		questionDTO.setOption1("op1");
		questionDTO.setOption2("op2");
		questionDTO.setOption3("op3");
		questionDTO.setOption4("op4");
		questionDTO.setQuestionDescription("What is Java ?");
		
	    questions=Arrays.asList(questionDTO,questionDTO);
		quizDTO=new QuizDTO();
		quizDTO.setQuizName("Java");
		quizDTO.setQuestions(questions);
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetails userDetails =  new User("osama", "123", authorities);
        jwt = jwtUtil.generateToken(userDetails);
	}
	
	@Test
	void test_deleteQuiz_IdExist() throws Exception {
		
		MvcResult result=this.mockMvc.perform(delete("/quizzes/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	    verify(quizService,times(1)).deleteQuiz(anyInt());
	}

	@Test
	void test_deleteQuiz_IdNotExist() throws Exception {
		doThrow(QuestionNotFoundException.class).when(quizService).deleteQuiz(anyInt());
		MvcResult result=this.mockMvc.perform(delete("/quizzes/8").header("Authorization", "Bearer " + jwt)).andReturn();
	    assertEquals(400,result.getResponse().getStatus());
	}
	
	@Test
	void test_viewQuizzzes() throws Exception {
		int status=this.mockMvc.perform(get("/quizzes").header("Authorization", "Bearer " + jwt)).andReturn().getResponse().getStatus();
		assertEquals(200, status);

	}
	
	@Test
	void test_addQuiz() throws Exception
	{	
		String quizDTOJson= objectMapper.writeValueAsString(quizDTO);
		MvcResult result=mockMvc.perform(post("/quizzes").header("Authorization", "Bearer " + jwt).contentType(MediaType.APPLICATION_JSON)
                .content(quizDTOJson)).andReturn();
		 
		 assertEquals(201,result.getResponse().getStatus());
	}
	
	
	@Test
	void test_getQuizQuestions_QuizIdExist_QuestionsExist() throws Exception
	{
		when(quizService.getQuestionsByQuizId(anyInt())).thenReturn(questions);
		MvcResult result=mockMvc.perform(get("/quizzes/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}
	@Test
	void test_getQuizQuestions_QuizIdExist_QuestionsNoExist() throws Exception
	{
		when(quizService.getQuestionsByQuizId(anyInt())).thenThrow(NoQuestionsFoundException.class);
		MvcResult result=mockMvc.perform(get("/quizzes/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(400, result.getResponse().getStatus());
	}
	@Test
	void test_getQuizQuestions_QuizIdNotExist() throws Exception
	{
		when(quizService.getQuestionsByQuizId(anyInt())).thenThrow(QuizNotFoundException.class);
		MvcResult result=mockMvc.perform(get("/quizzes/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(400, result.getResponse().getStatus());
	}
	
}
