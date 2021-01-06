package com.apiadmincore.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryRedirectUnauthorizedHandler implements AuthenticationEntryPoint {

	private static final Logger LOG = LoggerFactory.getLogger(AuthEntryPointUnauthorizedHandler.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException 
	{
		LOG.error("Unauthorized error: {}", authException.getMessage());
		response.sendRedirect("/admin/auth/login");
	}

}