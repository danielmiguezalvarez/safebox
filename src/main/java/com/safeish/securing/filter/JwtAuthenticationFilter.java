package com.safeish.securing.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.safeish.securing.JwtTokenUtil;
import com.safeish.securing.SecurityConstants;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public JwtAuthenticationFilter(String requiresAuthenticationRequestMatcher, AuthenticationManager manager) {
		super(requiresAuthenticationRequestMatcher);

		setAuthenticationManager(manager);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException, IOException, ServletException {

		String id = request.getParameter("id");
		String password = request.getParameter("password");
				
		UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(id, password);
		return getAuthenticationManager().authenticate(user);
	}

	

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String jwt = JwtTokenUtil.getInstance().generateToken(authResult.getName());
		response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + jwt);
		response.getWriter().write(SecurityConstants.TOKEN_PREFIX + jwt);

	}
	
	@Override	
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		
		Boolean respose = false;
		if(failed instanceof LockedException) {
			response.setStatus(423);
			response.getWriter().write("Safebox locked");
			respose = true;
		}
		if(failed instanceof BadCredentialsException) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Unauthorized access");
			respose = true;
		}
		
		if(failed instanceof InternalAuthenticationServiceException) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("Requested safebox does not exist");
			respose = true;
		}
		
		if(failed instanceof Exception && !respose) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Unexpected API error");
		}
				
		
	}
	
	
	
}
