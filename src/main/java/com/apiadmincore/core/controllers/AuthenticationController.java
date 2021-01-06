package com.apiadmincore.core.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiadmincore.core.api.payload.request.LoginRequestPayload;
import com.apiadmincore.core.api.payload.response.AuthenticationPayloadResponse;
import com.apiadmincore.core.api.payload.response.MessageResponse;
import com.apiadmincore.core.repository.RoleRepository;
import com.apiadmincore.core.repository.UserRepository;
import com.apiadmincore.core.security.JwtUtils;
import com.apiadmincore.core.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value="/v1/auth",produces = "application/json")
public class AuthenticationController 
{
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/login")
	public final ResponseEntity<AuthenticationPayloadResponse> authenticate(@Valid @RequestBody LoginRequestPayload loginRequest)
	{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new AuthenticationPayloadResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 roles.get(0)));
	}
	
	@GetMapping("/valor")
	public final ResponseEntity<MessageResponse> log()
	{
		return ResponseEntity.ok(new MessageResponse("Hello World", 0));
	}
}
