package com.apiadmincore.core.api.payload.response;

public class AuthenticationPayloadResponse {
	
	private String jwt;
	
	private String id;
	
	private String email;
	
	private String role;
	
	public AuthenticationPayloadResponse() {}
	
	public AuthenticationPayloadResponse(String jwt, String id, String email, String role)
	{
		this.jwt = jwt;
		this.id = id;
		this.email = email;
		this.role = role;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRoles(String role) {
		this.role = role;
	}
	
	

}
