package com.apiadmincore.core.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.apiadmincore.core.admin.helpers.Count;
import com.apiadmincore.core.models.Role;
import com.apiadmincore.core.models.User;
import com.apiadmincore.core.repository.RoleRepository;

@Service
public class RoleService extends BaseServices<Role, RoleRepository>{
	
	
	public final List<Role> list(int page, long limit, String query, Direction direction, String sortField)
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
		
		AggregationResults<Role> result = mongoTemplate.aggregate(aggregation, "roles", Role.class);
		
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
			return mongoTemplate.aggregate(aggregation, "roles", Count.class).getMappedResults().get(0).getTotal();
		}catch(Exception e){
			return 0;
		}
	}
}
