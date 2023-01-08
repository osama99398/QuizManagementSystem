package com.epam.quizapp.dto;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
	
	private static final long serialVersionUID = 5695483852669892030L;
	private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
