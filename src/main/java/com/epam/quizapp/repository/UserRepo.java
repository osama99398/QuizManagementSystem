package com.epam.quizapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.quizapp.model.User;

public interface UserRepo extends JpaRepository<User,Integer> {
	
	public Optional<User> findByUserEmailAndUserPassword(String userEmail, String userPassword);

	public Optional<User> findByUserName(String username);
}
