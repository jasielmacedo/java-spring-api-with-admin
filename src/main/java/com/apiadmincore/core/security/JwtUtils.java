package com.apiadmincore.core.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.apiadmincore.core.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils 
{
	@Value("${apiadmin.security.jwt.jwtSecret}")
	private String jwtSecret;
	
	@Value("${apiadmin.security.jwt.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String generateJwtToken(Authentication authentication) 
	{

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String getUserNameFromJwtToken(String token) 
	{
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}
	
	public boolean validateToken(String token)
	{
		Claims claims = getClaims(token);
		
		if (claims != null) 
		{
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate))
			{
				return true;
			}
		}
		return false;
	}
	
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		}
		catch (Exception e) {
			return null;
		}
	}
}
