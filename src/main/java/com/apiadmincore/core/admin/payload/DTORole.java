package com.apiadmincore.core.admin.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.apiadmincore.core.models.ERole;
import com.apiadmincore.core.models.Role;

public class DTORole 
{
	@NotBlank
	@Size(min=2)
	private String name;
	
	@NotBlank
	private String role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ERole getRoleEnum() {
		return ERole.valueOf(role);
	}
	
	public String getRole()
	{
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public Role buildRole()
	{
		return new Role(this.name, ERole.valueOf(this.role));
	}
}
