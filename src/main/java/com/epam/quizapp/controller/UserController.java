package com.epam.quizapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.quizapp.dto.UserDTO;
import com.epam.quizapp.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<List<UserDTO>> fetchAllUsers() {
		return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") int userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
		return new ResponseEntity<>(userService.addUser(userDTO), HttpStatus.CREATED);
	}
	
	
	
}
