package com.epam.quizapp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.epam.quizapp.service.UserService;

@Configuration
public class SecurityConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public AuthenticationManager getAuthenticationManager() {
		return new ProviderManager(List.of(getAuthenticationProvider()));
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
		.authenticationManager(getAuthenticationManager()).formLogin().and()
		.logout(logout -> logout.logoutUrl("/logout")).exceptionHandling().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().httpBasic();

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().antMatchers("/authenticate");
	}

	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		provider.setAuthoritiesMapper(getAuthoritiesMapper());
		return provider;
	}

	@Bean
	public GrantedAuthoritiesMapper getAuthoritiesMapper() {

		SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();

		mapper.setConvertToUpperCase(true);

		//mapper.setDefaultAuthority("USER");

		return mapper;
	}

}
