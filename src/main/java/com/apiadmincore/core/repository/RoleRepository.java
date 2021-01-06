package com.apiadmincore.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apiadmincore.core.models.ERole;
import com.apiadmincore.core.models.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> 
{
  Role findByRole(ERole role);  
  Boolean existsByRole(ERole role);
}
