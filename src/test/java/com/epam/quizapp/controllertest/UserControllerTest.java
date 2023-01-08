package com.epam.quizapp.controllertest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.epam.quizapp.dto.UserDTO;
import com.epam.quizapp.exceptions.UserNotFoundException;
import com.epam.quizapp.service.UserService;
import com.epam.quizapp.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtil jwtUtil;

	@MockBean
	private UserService userService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private UserDTO userDTO;

	private String userDTOJson;
	

	private List<UserDTO> users;
	private String jwt;

	@BeforeEach
	void setUpObject() throws JsonProcessingException {
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ADMIN"));
		UserDetails userDetails = new org.springframework.security.core.userdetails.User("osama", "123", authorities);
		jwt = jwtUtil.generateToken(userDetails);

		
		userDTO = new UserDTO();
		userDTO.setUserEmail("osam@epam");
		userDTO.setUserName("osama");
		userDTO.setUserPassword("123");
		userDTO.setUserRole("ADMIN");
		userDTOJson = objectMapper.writeValueAsString(userDTO);
		users = Arrays.asList(userDTO);

	}

	@Test
	void test_findAllUser() throws Exception {
		when(userService.findAllUsers()).thenReturn(users);
		MvcResult result = this.mockMvc.perform(get("/users").header("Authorization", "Bearer " + jwt)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void test_getUserById_IdExist() throws Exception {
		when(userService.getUserById(anyInt())).thenReturn(userDTO);
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/users/1").header("Authorization", "Bearer " + jwt)).andReturn().getResponse();
		assertEquals(200, response.getStatus());
		assertEquals(userDTOJson, response.getContentAsString());
	}

	@Test
	void test_getUserById_IdNotExist() throws Exception {
		when(userService.getUserById(anyInt())).thenThrow(UserNotFoundException.class);
		MockHttpServletResponse response = this.mockMvc
				.perform(get("/users/1").header("Authorization", "Bearer " + jwt)).andReturn().getResponse();
		assertEquals(400, response.getStatus());
	}

	@Test
	void test_addUser() throws Exception {
		when(userService.addUser(userDTO)).thenReturn(userDTO);

		MvcResult result = mockMvc.perform(post("/users").header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(userDTOJson)).andReturn();

		assertEquals(201, result.getResponse().getStatus());
	}

}
