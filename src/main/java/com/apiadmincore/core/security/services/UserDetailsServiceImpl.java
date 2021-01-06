package com.apiadmincore.core.security.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiadmincore.core.models.User;
import com.apiadmincore.core.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		
		User user = userRepository.findByEmail(username);
		if(user == null)
			throw new UsernameNotFoundException(username);
		
		return UserDetailsImpl.build(user);
	}

}
