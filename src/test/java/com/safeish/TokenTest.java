package com.safeish;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.safeish.securing.JwtTokenUtil;


class TokenTest {
	
	
	@Test
	public void testGenerateTokenAndGetUser() {
		
		String token = JwtTokenUtil.getInstance().generateToken("daniel");		
		
		String nombre = JwtTokenUtil.getInstance().getUsernameFromToken(token);		
		assertEquals(nombre, "daniel");
		
	}
		
}
