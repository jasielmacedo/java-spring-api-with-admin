package com.progamercollege.core.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="configuration")
public class SiteConfiguration 
{
	@Id
	private String id;
	
	@NotBlank
	@Indexed(unique=true)
	private String name;
	
	@NotBlank
	private String objectValue;
	
	public SiteConfiguration() {}
	
	public SiteConfiguration(String name, String val)
	{
		this.name = name;
		this.objectValue = val;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getObjectValue() {
		return objectValue;
	}

	public final void setObjectValue(String objectValue) {
		this.objectValue = objectValue;
	}
	
	
}
