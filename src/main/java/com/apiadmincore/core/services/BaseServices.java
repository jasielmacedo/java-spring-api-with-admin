package com.apiadmincore.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class BaseServices<E,T extends MongoRepository<E, String>> 
{
	@Autowired
	protected T repository;
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	
	public E Create(E newElement)
	{
		return this.repository.save(newElement);
	}
	
	public E Update(E element)
	{
		return this.repository.save(element);
	}
	
	public E findById(String id)
	{
		Optional<E> p = this.repository.findById(id);
		if(p.isPresent())
			return p.get();
		return null;
	}
	
	public List<E> getAll()
	{
		return this.repository.findAll();
	}
	
	public void delete(String id)
	{
		this.delete(id);
	}
	
	public long count()
	{
		return this.repository.count();
	}
}
