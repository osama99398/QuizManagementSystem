package com.epam.quizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.quizapp.dto.AuthenticationRequest;
import com.epam.quizapp.dto.AuthenticationResponse;
import com.epam.quizapp.exceptions.InvalidCredentialsException;
import com.epam.quizapp.service.UserService;
import com.epam.quizapp.util.JwtUtil;


@RestController
public class AuthenticateController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws InvalidCredentialsException {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("Incorrect username or password");

		}

		final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok((new AuthenticationResponse(jwt)));
	}
}
