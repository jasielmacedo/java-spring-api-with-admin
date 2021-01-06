package com.apiadmincore.core.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminHomeController 
{
	@RequestMapping(value = {"","/"})
	public final ModelAndView Index()
	{
		ModelAndView viewReturn = new ModelAndView();
		viewReturn.setViewName("home/index");
		return viewReturn;
	}
}
