package com.lcl.scs.sapmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lcl.scs.sapmanagement.model.FlatData;

@Repository
public interface FlatDataRepository extends MongoRepository<FlatData, String> {

}
