package com.apiadmincore.core.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "roles")
public class Role 
{
	@Id
	private String id;
	
	@NotBlank
	@Size(min=2, max=48)
	private String name;
	
	@NotNull
	private ERole role;
	
	
	@CreatedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createdDate = new Date();
	
	@LastModifiedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date updatedDate = new Date();
	
	public Role() {}
	
	public Role(String name, ERole role)
	{
		this.name = name;
		this.role = role;
	}
	
	public String getId()
	{
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ERole getRole() {
		return role;
	}

	public void setRole(ERole role) {
		this.role = role;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	
	
}
