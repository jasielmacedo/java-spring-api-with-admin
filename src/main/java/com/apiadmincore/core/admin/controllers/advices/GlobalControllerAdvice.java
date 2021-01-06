package com.apiadmincore.core.admin.controllers.advices;


import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.apiadmincore.core.models.SiteConfiguration;
import com.apiadmincore.core.services.SiteConfigurationService;

@ControllerAdvice
public class GlobalControllerAdvice 
{
	@Autowired
	private SiteConfigurationService siteConfigService;
	
	@ModelAttribute("config")
	public Map<String,Object> populateConfig()
	{
		return siteConfigService.getAll().stream().collect(Collectors.toMap(SiteConfiguration::getId,SiteConfiguration::getObjectValue));
	}
}
