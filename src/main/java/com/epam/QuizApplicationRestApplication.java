package com.epam;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QuizApplicationRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizApplicationRestApplication.class, args);
	}
	
	@Bean
	public ModelMapper getmodelMapper() {
		return new ModelMapper();
	}

	
}
