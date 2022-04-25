package com.lcl.scs.dallas.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lcl.scs.dallas.model.DallasDetail;

public interface DetailDao {
	void batchInsert(List<DallasDetail> t, int batch);
}
