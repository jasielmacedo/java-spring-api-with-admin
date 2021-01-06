package com.apiadmincore.core.security.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.apiadmincore.core.models.ERole;
import com.apiadmincore.core.models.User;

public class UserDetailsImpl implements UserDetails 
{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String email;
	private String password;
	private Boolean enabled;
	private Boolean locked;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsImpl() {
	}
	
	public UserDetailsImpl(String id, String email, String password,Boolean enabled, Boolean locked, List<GrantedAuthority> authorities) 
	{
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.locked = locked;
		this.authorities = authorities;
	}
	
	public static UserDetailsImpl build(User user)
	{
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getRole().toString()));
		
		return new UserDetailsImpl(
			user.getId().toString(),
			user.getEmail(),
			user.getPassword(),
			user.isEnabled(),
			user.isLocked(),
			authorities
		);
	}

	public String getId() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public boolean hasRole(ERole role) {
		return getAuthorities().contains(new SimpleGrantedAuthority(role.toString()));
	}
}
