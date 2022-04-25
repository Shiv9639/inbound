package com.lcl.scs.sapmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lcl.scs.sapmanagement.model.ArticleMaster;
import com.lcl.scs.sapmanagement.model.StoreMaster;


@Repository
public interface ArticleMasterRepository extends MongoRepository<ArticleMaster, String>{

	ArticleMaster findByarticleNumber(String articleNumber);
	

}
