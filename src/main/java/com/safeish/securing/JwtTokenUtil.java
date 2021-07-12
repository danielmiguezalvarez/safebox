package com.safeish.securing;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.safeish.securing.model.UserAuthenticated;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JwtTokenUtil implements Serializable {

	private static JwtTokenUtil jwtTokenUtil;

	public static JwtTokenUtil getInstance() {

		if (JwtTokenUtil.jwtTokenUtil == null) {
			JwtTokenUtil.jwtTokenUtil = new JwtTokenUtil();
		}

		return JwtTokenUtil.jwtTokenUtil;

	}

	private JwtTokenUtil() {
	}

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 3 * 60;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET.getBytes()).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(String subject) {

		return Jwts.builder().setSubject(subject).setAudience(SecurityConstants.TOKEN_AUDIENCE)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET.getBytes()).compact();

	}

	public Authentication validateToken(HttpServletRequest request) {

		var user = new UserAuthenticated("");
		user.setAuthenticated(false);

		String token = request.getHeader(SecurityConstants.TOKEN_HEADER);

		String name = getNameFromToken(token);

		if (!"".equals(name)) {
			user.setName(name);
			user.setAuthenticated(true);
		}
		return user;
	}

	public String getUserNameFromRequestHeader(String token) {
		return getNameFromToken(token);

	}


	private String getNameFromToken(String token) {

		if (token != null && token.contains(SecurityConstants.TOKEN_PREFIX)) {
			String realToken = getRealToken(token);

			try {
				String username = getUsernameFromToken(realToken);

				if (username != null && !isTokenExpired(realToken)) {
					return username;
				}

			} catch (ExpiredJwtException e) {
				System.out.println(" Token expired ");
			} catch (SignatureException e) {
				System.out.println("calculating a signature or verifying an existing signature of a JWT failed");
			} catch (Exception e) {
				System.out.println(" Some other exception in JWT parsing ");
			}
		}
		return "";

	}
	
	private String getRealToken(String token) {
		if (token.contains(SecurityConstants.TOKEN_PREFIX)) {
			String realToken = token.replace(SecurityConstants.TOKEN_PREFIX, "");
			return realToken;
		}
		return "";
	}


}
