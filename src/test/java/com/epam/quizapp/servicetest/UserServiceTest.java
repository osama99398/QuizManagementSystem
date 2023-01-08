package com.epam.quizapp.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.epam.quizapp.dto.Converter;
import com.epam.quizapp.dto.UserDTO;
import com.epam.quizapp.exceptions.UserNotFoundException;
import com.epam.quizapp.model.AuthGroup;
import com.epam.quizapp.model.User;
import com.epam.quizapp.repository.AuthGroupRepo;
import com.epam.quizapp.repository.UserRepo;
import com.epam.quizapp.service.UserService;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepo userRepository;
	@Mock
	private AuthGroupRepo authGroupRepository;
	
	@InjectMocks
	private UserService  userService;
	
	@Mock
	private Converter converter;
	
	private User user;
	private UserDTO userDTO;
	private List<User> users;
	
	@BeforeEach
	void setUpObjects()
	{
        user=new User();
		
		user.setUserEmail("osama@epam.com");
		user.setUserName("osama");
		user.setUserPassword("123456");
		
		userDTO=new UserDTO();
		userDTO.setUserEmail("osama@epam.com");
		userDTO.setUserName("osama");
		userDTO.setUserPassword("123456");
		userDTO.setUserRole("ADMIN");
		users=Arrays.asList(user,user);
	}
	
	@Test
	void test_addUser_EmailNotExists() {
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(converter.convertDTOtoEntity(any(UserDTO.class))).thenReturn(user);
		when(converter.convertEntitytoDTO(any(User.class))).thenReturn(userDTO);
		assertNotNull(userService.addUser(userDTO));
	}
	@Test
	void test_addUser_EmailExists() {
		when(converter.convertDTOtoEntity(any(UserDTO.class))).thenReturn(user);
		when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);
		assertThrows(UserNotFoundException.class, ()->userService.addUser(userDTO));
	}
	@Test
	void test_getUserById_IdExist()
	{
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(converter.convertEntitytoDTO(any(User.class))).thenReturn(userDTO);
		assertNotNull(userService.getUserById(1));
	}
	@Test
	void test_getUserById_IdNotExist()
	{
		when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class,()->userService.getUserById(1));
	}
	@Test
	void test_findAllUsers()
	{
		when(userRepository.findAll()).thenReturn(users);
		when(converter.convertEntitytoDTO(any(User.class))).thenReturn(userDTO);
		assertEquals(2, userService.findAllUsers().size());
	}
	
	
	@Test
	void test_loadUserByUsername_usernamenotExist() {
		when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, ()->userService.loadUserByUsername("ramu"));
	}
	@Test
	void test_loadUserByUsername_usernameExist() {
		when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));
		AuthGroup authGroup=new AuthGroup();
		authGroup.setUsername("osama");
		authGroup.setAuthGroup("ADMIN");
		when(authGroupRepository.findByUsername(anyString())).thenReturn(Arrays.asList(authGroup));
		assertNotNull(userService.loadUserByUsername("osama"));
	}
	
	
	
}
