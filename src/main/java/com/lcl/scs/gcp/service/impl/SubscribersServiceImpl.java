package com.lcl.scs.gcp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.lcl.scs.subscribers.model.Subscribers;

@Service
public class SubscribersServiceImpl implements SubscribersService{

	@Autowired
	private MongoTemplate  mongoTemplate;
	@Override
	public Subscribers findByName(String name) {
		Query query = new Query(Criteria.where("name").is(name));
		return mongoTemplate.findOne(query, Subscribers.class,"subscribers");
	}

	@Override
	public List<Subscribers> findBySubscribername(String subscribername) {
		Query query = new Query(Criteria.where("subscribername").is(subscribername));
		return mongoTemplate.find(query, Subscribers.class, "subscribers");
	}

	@Override
	public Subscribers save(Subscribers subscribers) {
		return mongoTemplate.save(subscribers, "subscribers");
	}

}
