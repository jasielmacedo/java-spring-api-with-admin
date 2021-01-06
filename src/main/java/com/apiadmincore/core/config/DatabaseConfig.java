package com.apiadmincore.core.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.apiadmincore.core.services.DbRecoveryService;

@Configuration
@EnableMongoAuditing
public class DatabaseConfig 
{
	@Autowired
	private DbRecoveryService dbRecoveryService;
	
	@Bean
	public boolean InstantiateDatabase() throws ParseException 
	{
		return this.dbRecoveryService.InstantiateInitialDatabaseInfo();
	}
}
