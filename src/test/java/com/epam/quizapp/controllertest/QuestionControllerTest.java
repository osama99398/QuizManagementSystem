package com.epam.quizapp.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
import com.epam.quizapp.exceptions.NoQuestionsFoundException;
import com.epam.quizapp.exceptions.QuestionNotFoundException;
import com.epam.quizapp.service.QuestionService;
import com.epam.quizapp.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private QuestionService questionService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private QuestionDTO questionDTO;
	
	@Autowired
	private JwtUtil jwtUtil;

	private String jwt;
	
	@BeforeEach
	void setUp() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ADMIN"));
		UserDetails userDetails = new User("osama", "123", authorities);
		jwt = jwtUtil.generateToken(userDetails);

		questionDTO = new QuestionDTO();
		questionDTO.setQuestionId(1);
		questionDTO.setQuestionDescription("What is java");
		questionDTO.setOption1("op1");
		questionDTO.setOption2("op2");
		questionDTO.setOption3("op3");
		questionDTO.setOption4("op4");
		questionDTO.setAnswer(2);
		questionDTO.setQuizName("Java");
		
	}
	
	@Test
	void testDeleteQuestionIdExist() throws Exception {

		MvcResult result = this.mockMvc.perform(delete("/questions/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
		verify(questionService, times(1)).deleteQuestion(anyInt());

	}

	@Test
	void testDeleteQuestionIdNotExist() throws Exception {

		doThrow(QuestionNotFoundException.class).when(questionService).deleteQuestion(anyInt());
		MvcResult result = this.mockMvc.perform(delete("/questions/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(400, result.getResponse().getStatus());
	}
	
	@Test
	void testGetQuestionIdExist() throws Exception {
		String questionDTOJson = objectMapper.writeValueAsString(questionDTO);
		when(questionService.getQuestionById(anyInt())).thenReturn(questionDTO);
		MvcResult result = this.mockMvc.perform(get("/questions/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
		assertEquals(questionDTOJson, result.getResponse().getContentAsString());
	}

	@Test
	void testGetQuestionIdNotExist() throws Exception {
		when(questionService.getQuestionById(anyInt())).thenThrow(QuestionNotFoundException.class);
		MvcResult result = this.mockMvc.perform(get("/questions/1").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(400, result.getResponse().getStatus());
	}

	@Test
	void test_viewGetQuestions() throws Exception {

		List<QuestionDTO> questions = Arrays.asList(questionDTO);
		when(questionService.getQuestions()).thenReturn(questions);
		MvcResult result = mockMvc.perform(get("/questions").header("Authorization", "Bearer " + jwt)).andReturn();

		assertEquals(200, result.getResponse().getStatus());

	}

	@Test
	void test_viewGetQuestions_questionsNotExist() throws Exception {
		when(questionService.getQuestions()).thenThrow(NoQuestionsFoundException.class);
		MvcResult result = mockMvc.perform(get("/questions").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(400, result.getResponse().getStatus());
	}
	
	@Test
	void testEditQuestion() throws Exception {

		when(questionService.editQuestion(questionDTO)).thenReturn(questionDTO);
		String questionDTOJson = objectMapper.writeValueAsString(questionDTO);
		MvcResult result = mockMvc.perform(put("/questions").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(questionDTOJson)).andReturn();

		assertEquals(200, result.getResponse().getStatus());

	}
	
	@Test
	void testAddQuestion() throws Exception {

		String questionDTOJson = objectMapper.writeValueAsString(questionDTO);
		MvcResult result = mockMvc.perform(post("/questions").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(questionDTOJson)).andReturn();

		assertEquals(201, result.getResponse().getStatus());

	}

}
