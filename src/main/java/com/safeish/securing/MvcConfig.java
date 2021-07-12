package com.safeish.securing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.safeish.securing.filter.JwtAuthenticationFilter;
import com.safeish.securing.filter.JwtAuthorizationFilter;

@Configuration
public class MvcConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		PasswordEncoder codificador = PasswordEncoderFactories.createDelegatingPasswordEncoder();		
		auth.userDetailsService(userDetailsService).passwordEncoder(codificador);
			
	}
			
	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		
		http
	    .csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/safebox")
		.permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilterBefore(new JwtAuthenticationFilter("/safebox/open",authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);		
	}
	
}
