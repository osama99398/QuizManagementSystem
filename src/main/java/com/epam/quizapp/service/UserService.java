package com.epam.quizapp.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.epam.quizapp.dto.Converter;
import com.epam.quizapp.dto.UserDTO;
import com.epam.quizapp.exceptions.UserNotFoundException;
import com.epam.quizapp.model.AuthGroup;
import com.epam.quizapp.model.User;
import com.epam.quizapp.repository.AuthGroupRepo;
import com.epam.quizapp.repository.UserRepo;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private AuthGroupRepo authGroupRepo;
	
	public UserDTO addUser(UserDTO userDTO) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = converter.convertDTOtoEntity(userDTO);
		user.setUserPassword(encoder.encode(user.getUserPassword()));
		authGroupRepo.save(new AuthGroup(userDTO.getUserName(), userDTO.getUserRole()));
		UserDTO userDTO2;
		try {
			userDTO2 = converter.convertEntitytoDTO(userRepo.save(user));
		} catch (RuntimeException e) {
			throw new UserNotFoundException("User already Exist");
		}
		return userDTO2;
	}
	
	public UserDTO getUserById(int userId) {
		User user = userExistById(userId);
		return converter.convertEntitytoDTO(user);
	}
	
	public List<UserDTO> findAllUsers() {
		List<User> users = userRepo.findAll();
		return users.stream().map(u -> converter.convertEntitytoDTO(u)).toList();
	}
	
	public User userExistById(int userId) {
		return userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User Not Found With Id" + userId));
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("Cannot find username :" + username));
		List<AuthGroup> authorties = authGroupRepo.findByUsername(username);
		return new org.springframework.security.core.userdetails.User(
				user.getUserName(), user.getUserPassword()
				,authorties.stream().map(authGroup -> new SimpleGrantedAuthority(authGroup.getAuthGroup())).toList());
		
		
	}
	
}
