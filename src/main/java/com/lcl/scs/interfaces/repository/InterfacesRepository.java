package com.lcl.scs.interfaces.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lcl.scs.interfaces.model.Interfaces;

@Repository
public interface InterfacesRepository extends MongoRepository<Interfaces, String> {
	List<Interfaces> findBySource(String source);

	Interfaces findByRicefnum(String ricefnum);

	List<Interfaces> findAll();
}
