package com.apiadmincore.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.apiadmincore.core.models.ERole;
import com.apiadmincore.core.models.Role;
import com.apiadmincore.core.models.SiteConfiguration;
import com.apiadmincore.core.models.User;
import com.apiadmincore.core.repository.RoleRepository;
import com.apiadmincore.core.repository.SiteConfigurationRepository;
import com.apiadmincore.core.repository.UserRepository;

@Service
public class DbRecoveryService 
{
	private static final Logger LOG = LoggerFactory.getLogger(DbRecoveryService.class);
	
	@Autowired
	private SiteConfigurationRepository siteConfigRepository;
	
	@Autowired
	private SiteConfigurationService siteConfigurationService;
	
	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Value("${apiadmin.user.admin.name}")
	private String initialAdminName;
	
	@Value("${apiadmin.user.admin.email}")
	private String initialAdminEmail;
	
	@Value("${apiadmin.user.admin.username}")
	private String initialAdminUsername;
	
	@Value("${apiadmin.user.admin.password}")
	private String initialAdminPassword;
	
	public final boolean InstantiateInitialDatabaseInfo()
	{
		Role adminRole;
		
		if(!this.roleRepository.existsByRole(ERole.ROLE_ADMIN))
		{
			adminRole = new Role("Administration",ERole.ROLE_ADMIN);
			adminRole = this.roleRepository.save(adminRole);
			LOG.debug("Creating initial role");
		}else {			
			if(this.siteConfigRepository.existsByName(SiteConfigurationService.Config_masterAdminRole))
			{
				SiteConfiguration config = this.siteConfigRepository.findByName(SiteConfigurationService.Config_masterAdminRole);
				String idAdminRole = config.getObjectValue();
				

				if(this.roleRepository.existsById(idAdminRole))
					adminRole = this.roleRepository.findById(idAdminRole).get();
				else
				{
					adminRole = this.roleRepository.findByRole(ERole.ROLE_ADMIN);
					
					config.setObjectValue(adminRole.getId());
					this.siteConfigurationService.Update(config);
				}
			}else {
				adminRole = this.roleRepository.findByRole(ERole.ROLE_ADMIN);
				this.siteConfigurationService.Create(SiteConfigurationService.Config_masterAdminRole, adminRole.getId());
			}	
		}
		
		if(!this.userRepository.existsByEmail(initialAdminEmail))
		{
			User userAdmin = new User(this.initialAdminName, this.initialAdminUsername, this.initialAdminEmail, this.initialAdminPassword);
				
			userAdmin.setRole(adminRole);
			
			userAdmin = this.userService.CreateWithoutRole(userAdmin); 
		}
		
		return true;
	}
}
