package com.apiadmincore.core.models;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document
public class User 
{
	@Id
	private ObjectId id;
	
	@NotEmpty
	@Size(max = 50)
	private String name;
	
	@NotEmpty
	@Size(max = 50)
	@Indexed(unique=true)
	private String username;
	
	@NotEmpty
	@Email
	@Indexed(unique=true)
	private String email;
	
	@NotBlank
	@Size(max = 120)
	private String password;
	
	private boolean enabled;
	
	private boolean confirmed;
	
	private boolean locked;
	
	@CreatedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createdDate = new Date();
	
	@LastModifiedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date updatedDate = new Date();
	
	@DBRef
	private Role role;
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User() {}
	
	public User(String name, String username, String email, String password)
	{
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = true;
		this.confirmed = false;
		this.locked = false;
	}
	
	public String getId()
	{
		return id.toString();
	}

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}


	public Date getUpdatedDate() {
		return updatedDate;
	}


	public void setId(ObjectId id) {
		this.id = id;
	}
	
	

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String ToString()
	{
		return String.format("Name: %s, Email: %s, Username: %s", this.getName(), this.getEmail(), this.getUsername());
	}
}