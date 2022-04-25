package com.lcl.scs.sapmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lcl.scs.sapmanagement.model.VendorMaster;
@Repository
public interface VendorMasterRepository extends MongoRepository<VendorMaster, String>{
	
	VendorMaster findBySite(String site);
}
