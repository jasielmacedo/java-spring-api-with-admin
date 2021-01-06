package com.apiadmincore.core.repository;


import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apiadmincore.core.models.User;


@Primary
@Repository
public interface UserRepository extends MongoRepository<User, String> 
{
	User findByUsername(String username);
	User findByEmail(String email);
	
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}

