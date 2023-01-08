package com.epam.quizapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.quizapp.model.AuthGroup;


public interface AuthGroupRepo  extends JpaRepository<AuthGroup, Long> {
    List<AuthGroup> findByUsername(String username);

}
