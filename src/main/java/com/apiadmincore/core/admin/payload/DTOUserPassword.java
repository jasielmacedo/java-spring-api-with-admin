package com.apiadmincore.core.admin.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DTOUserPassword 
{
	@NotBlank
	@Size(min=2)
	private String oldPassword;
	
	@NotBlank
	@Size(min=2)
	private String newPassword;
	
	@NotBlank
	@Size(min=2)
	private String confirmPassword;
	
	@Autowired
	private PasswordEncoder bCrypt;

	public String getOldPassword() {
		return bCrypt.encode(oldPassword);
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
}
