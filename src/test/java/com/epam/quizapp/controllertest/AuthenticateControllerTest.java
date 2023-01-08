package com.epam.quizapp.controllertest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.epam.quizapp.dto.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticateControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper=new ObjectMapper();


	@Test
	void test_authenticate() throws Exception {
		
		AuthenticationRequest request=new AuthenticationRequest();
		request.setUsername("osama");
		request.setPassword("123");
		String requestJson= objectMapper.writeValueAsString(request);
		
		 MvcResult result=mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON)
	                .content(requestJson)).andReturn();
			 
			 assertEquals(200,result.getResponse().getStatus());
	    
	}
	@Test
	void test_authenticate_InvalidDetails() throws Exception {
		
		AuthenticationRequest request=new AuthenticationRequest();
		request.setUsername("abcd");
		request.setPassword("123456");
		String requestJson= objectMapper.writeValueAsString(request);
		
		 MvcResult result=mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON)
	                .content(requestJson)).andReturn();
			 
			 assertEquals(400,result.getResponse().getStatus());
	    
	}

}
