package com.apiadmincore.core.services;

import org.springframework.stereotype.Service;

import com.apiadmincore.core.models.SiteConfiguration;
import com.apiadmincore.core.repository.SiteConfigurationRepository;

@Service
public class SiteConfigurationService extends BaseServices<SiteConfiguration,SiteConfigurationRepository>
{
	public static final String Config_masterAdminRole = "config.masterAdminRoleId";
	public static final String Config_defaultUserRole = "config.defaultUserRole";
	public static final String Config_defaultSeoTitle = "config.defaultSeoTitle";
	public static final String Config_defaultSeoDescription = "config.defaultSeoDescription";
	
	private GlobalConfig globalConfig = new GlobalConfig();
	
	public final SiteConfiguration Create(String name, String val)
	{
		SiteConfiguration c = new SiteConfiguration(name, val);
		return this.Create(c);
	}
	
	public final SiteConfiguration getByName(String name)
	{
		return this.repository.findByName(name);
	}
	
	public final void setGlobalConfig(GlobalConfig gb)
	{
		this.globalConfig = gb;
	}
	
	public final GlobalConfig globalConfig()
	{
		return this.globalConfig;
	}
	
	public class GlobalConfig
	{
		private String masterAdminRoleId;
		private String defaultUserRole;
		
		private String defaultSeoTitle;
		private String defaultSeoDescription;

		public String getMasterAdminRoleId() {
			return masterAdminRoleId;
		}

		public void setMasterAdminRoleId(String masterAdminRoleId) {
			this.masterAdminRoleId = masterAdminRoleId;
		}

		public final String getDefaultUserRole() {
			return defaultUserRole;
		}

		public final void setDefaultUserRole(String defaultUserRole) {
			this.defaultUserRole = defaultUserRole;
		}

		public String getDefaultSeoTitle() {
			return defaultSeoTitle;
		}

		public void setDefaultSeoTitle(String defaultSeoTitle) {
			this.defaultSeoTitle = defaultSeoTitle;
		}

		public String getDefaultSeoDescription() {
			return defaultSeoDescription;
		}

		public void setDefaultSeoDescription(String defaultSeoDescription) {
			this.defaultSeoDescription = defaultSeoDescription;
		}
		
		
	}
}
