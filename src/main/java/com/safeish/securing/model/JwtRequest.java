package com.safeish.securing.model;

import java.io.Serializable;
import java.util.UUID;

public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	private String safeboxId;
	private String password;
	private UUID id;
	
	
	public JwtRequest() {};
	

	public JwtRequest(String safeboxId, String password) {
		this.safeboxId = safeboxId;
		this.password = password;
	}


	public String getSafeboxId() {
		return safeboxId;
	}

	public void setSafeboxId(String safeboxId) {
		this.safeboxId = safeboxId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}


}
