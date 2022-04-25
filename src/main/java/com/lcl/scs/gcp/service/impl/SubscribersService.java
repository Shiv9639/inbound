package com.lcl.scs.gcp.service.impl;

import java.util.List;

import com.lcl.scs.subscribers.model.Subscribers;

public interface SubscribersService {
	
	Subscribers findByName(String name);
	List<Subscribers> findBySubscribername(String subscribername);
	Subscribers save(Subscribers subscribers);
	

}
