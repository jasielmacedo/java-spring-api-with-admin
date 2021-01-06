package com.apiadmincore.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public final String findLoggedInUsername() 
	{
		Object user = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if(user instanceof UserDetails)
		{
			return ((UserDetails)user).getUsername();
		}
		return null;
	}

	public final boolean Login(String username, String password) 
	{
        try
        {
        	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        	
        	authenticationManager.authenticate(usernamePasswordAuthenticationToken);

	        if (usernamePasswordAuthenticationToken.isAuthenticated()) 
	        {
	            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	            return true;
	        }
        }catch(Exception e)
        {
        	LOG.error(e.getMessage());
        }
        
        return false;
	}

}
