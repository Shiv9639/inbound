package com.lcl.scs.interfaces.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lcl.scs.interfaces.model.Interfaces;

@Service
public interface InterfacesService {
 List<Interfaces> findAll();
 Interfaces findByRicefnum(String ricefnum);
 List<Interfaces> findBySource(String source);
 void saveOrUpdateInterfaces(Interfaces i);
}
