package com.apiadmincore.core.admin.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.apiadmincore.core.models.User;


public class DTOUser {
	@NotBlank
	@Size(min=2)
	private String name;
	
	@NotBlank
	@Size(min=2, max=48)
	private String username;
	
	@NotBlank
	@Email
	private String email;
	
	private String password;
	
	@NotBlank
	private String role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Build object User
	 * @return User 
	 */
	public User buildUser()
	{
		return new User(this.name,this.username,this.email,this.password);
	}
	
	public User updateUser(User currentUser)
	{
		currentUser.setUsername(this.username);
		currentUser.setName(this.name);
		currentUser.setEmail(this.email);
		
		return currentUser;
	}
}
