package com.apiadmincore.core.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.apiadmincore.core.security.AuthEntryPointUnauthorizedHandler;
import com.apiadmincore.core.security.AuthEntryRedirectUnauthorizedHandler;
import com.apiadmincore.core.security.JwtAuthTokenFilter;
import com.apiadmincore.core.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
{
	private static final String[] PUBLIC_API_MATCHERS_GET = {
			"/v1/bla",
	};

	private static final String[] PUBLIC_API_MATCHERS_POST = {
			"/v1/auth/login",
	};
	
	@Order(1)
	@Configuration
	public class ApiSecurityConfig extends WebSecurityConfigurerAdapter
	{		
		@Autowired
	    private UserDetailsServiceImpl userDetailsService;
		
		@Autowired
		private AuthEntryPointUnauthorizedHandler unauthorizedHandler;
		
		
		
		@Override
		public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
			authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Bean
		public PasswordEncoder passwordEncoder() 
		{
			return new BCryptPasswordEncoder();
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/v1/**").cors().and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		        .authorizeRequests()
					.antMatchers(HttpMethod.POST,PUBLIC_API_MATCHERS_POST).permitAll()
					.antMatchers(HttpMethod.GET, PUBLIC_API_MATCHERS_GET).permitAll()
				.anyRequest().authenticated();
			
			http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		}
		
		@Bean
		public JwtAuthTokenFilter authenticationJwtTokenFilter() {
			return new JwtAuthTokenFilter();
		}
		
		@Bean
		CorsConfigurationSource corsConfigurationSource() {
			CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
			configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
			final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", configuration);
			return source;
		}
	}
	
	@Order(2)
	@Configuration
	public class AdminSecurityConfig extends WebSecurityConfigurerAdapter
	{
		
		@Autowired
		private AuthEntryRedirectUnauthorizedHandler unauthorizedRedirectHandler;
		
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/admin/**").cors().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedRedirectHandler).and()
				.authorizeRequests()
					.antMatchers("/static/**", "/admin/auth/login", "/admin/auth/reset").permitAll()
					.and()
					.logout()
					.logoutUrl("/admin/auth/logout")
			        .permitAll();
			        
			http.exceptionHandling().authenticationEntryPoint(unauthorizedRedirectHandler).and()
			.authorizeRequests().anyRequest().authenticated();     
			
		}
	}

}
