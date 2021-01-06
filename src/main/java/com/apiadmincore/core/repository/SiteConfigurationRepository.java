package com.apiadmincore.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apiadmincore.core.models.SiteConfiguration;

@Repository
public interface SiteConfigurationRepository extends MongoRepository<SiteConfiguration, String>
{
	SiteConfiguration findByName(String name);
	Boolean existsByName(String name);
}
