package com.apiadmincore.core.api.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class LoginRequestPayload {
	@NotBlank
	private String email;
	
	@NotEmpty
	private String password;

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}


	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}
}
