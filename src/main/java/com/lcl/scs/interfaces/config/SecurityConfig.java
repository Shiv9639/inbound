package com.lcl.scs.interfaces.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
public class SecurityConfig {//extends WebSecurityConfigurerAdapter {

//	private String ESB_USER = System.getenv("ESB_USER");
//	private String ESB_PASSWORD = System.getenv("ESB_PASSWORD");
//	private String ESB_USER_ROLE = System.getenv("ESB_USER_ROLE");
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// TODO Auto-generated method stub
//		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
//	}
//
//	@Autowired
//	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		// TODO Auto-generated method stub
//		// super.configure(auth);
//		auth.inMemoryAuthentication().withUser(ESB_USER).password(passwordEncoder().encode(ESB_PASSWORD))
//				.authorities(ESB_USER_ROLE);
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}
