package com.apiadmincore.core.services;


import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apiadmincore.core.admin.helpers.Count;
import com.apiadmincore.core.models.ERole;
import com.apiadmincore.core.models.User;
import com.apiadmincore.core.repository.RoleRepository;
import com.apiadmincore.core.repository.UserRepository;

@Service
public class UserService extends BaseServices<User,UserRepository>
{
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder bCrypt;
	
	@Override
	public User Create(User user)
	{
		user.setPassword(bCrypt.encode(user.getPassword()));

		if(user.getRole() == null)
			user.setRole(roleRepository.findByRole(ERole.ROLE_USER));
		
		return this.repository.save(user);
	}
	
	public User CreateWithoutRole(User user)
	{
		user.setPassword(bCrypt.encode(user.getPassword()));	
		return this.repository.save(user);
	}
	
	public final boolean existsWithEmail(String email)
	{
		return this.repository.existsByEmail(email);
	}
	
	public final boolean existsByUsername(String username)
	{
		return this.repository.existsByUsername(username);
	}
	
	public final User findByEmail(String email)
	{
		return this.repository.findByEmail(email);
	}
	
	public final List<User> list(int page, long limit, String query, Direction direction, String sortField)
	{
		long skip = limit * (page-1);

		LimitOperation maxElements = Aggregation.limit(limit);
		SkipOperation skipOperation = Aggregation.skip(skip);
		SortOperation sort = Aggregation.sort(Sort.by(direction,sortField));
		
		Aggregation aggregation;
		
		if(!query.equals(""))
		{
			MatchOperation matchOperation = Aggregation.match(Criteria.where("name").regex(query,"i"));
			
			aggregation = Aggregation.newAggregation(matchOperation,sort,skipOperation,maxElements);
		}else {
			aggregation = Aggregation.newAggregation(sort,skipOperation,maxElements);
		}
		
		AggregationResults<User> result = mongoTemplate.aggregate(aggregation, "user", User.class);
		
		return result.getMappedResults();
	}
	
	public long count(String query)
	{
		if(query.equals(""))
			return this.count();
		
		MatchOperation matchOperation = Aggregation.match(Criteria.where("name").regex(query, "i"));
		
		Aggregation aggregation = Aggregation.newAggregation(matchOperation,
				Aggregation.group().count().as("total"));
		
		try
		{
			return mongoTemplate.aggregate(aggregation, "user", Count.class).getMappedResults().get(0).getTotal();
		}catch(Exception e){
			return 0;
		}
	}
}
