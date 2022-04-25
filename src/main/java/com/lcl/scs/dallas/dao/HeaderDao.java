package com.lcl.scs.dallas.dao;

import java.util.List;

import com.lcl.scs.dallas.model.DallasHeader;

public interface HeaderDao {
	void batchInsert(List<DallasHeader> t, int batch);
}
