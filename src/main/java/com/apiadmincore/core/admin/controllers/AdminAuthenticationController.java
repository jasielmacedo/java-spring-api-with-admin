package com.apiadmincore.core.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.apiadmincore.core.admin.payload.DTOLogin;
import com.apiadmincore.core.services.AuthService;

@Controller
@RequestMapping("/admin/auth")
public class AdminAuthenticationController 
{
	private static final Logger LOG = LoggerFactory.getLogger(AdminAuthenticationController.class);
	
	@Autowired
	private AuthService authService;

	
	@GetMapping("/login")
	public final ModelAndView Login(DTOLogin loginForm)
	{
		ModelAndView viewReturn = new ModelAndView();
		viewReturn.setViewName("auth/login");
		viewReturn.addObject("errorNum", 0);
		
		return viewReturn;
	}
	
	@PostMapping("/login")
	public final String Login(@Valid DTOLogin loginForm, BindingResult bindingResult, Model model)
	{		
		
		if(bindingResult.hasErrors())
		{
			
			model.addAttribute("errorNum", 1);
			model.addAttribute("error", "Preencha os campos corretamente");
			
			return "auth/login";
		}		
		
		
		if(authService.Login(loginForm.getEmail(), loginForm.getPassword()))
		{
			return "redirect:/admin/";
		}else {
			model.addAttribute("errorNum", 2);
			model.addAttribute("error", "Email ou senha inválidos");
		}
		
		return "auth/login";
	}
	
	@GetMapping("/logout")
	public final String Logout(HttpServletRequest request, HttpServletResponse response)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null)
	    {    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/admin/auth/login?logout=true";
	}
}
