package com.lcl.scs.interfaces.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcl.scs.interfaces.model.Interfaces;
import com.lcl.scs.interfaces.repository.InterfacesRepository;
import com.lcl.scs.interfaces.service.InterfacesService;

@Service
public class InterfacesServiceImpl implements InterfacesService {
	@Autowired
	private InterfacesRepository interfacesRepository;

	@Override
	public List<Interfaces> findAll() {
		// TODO Auto-generated method stub
		return interfacesRepository.findAll();
	}

	@Override
	public Interfaces findByRicefnum(String ricefnum) {
		// TODO Auto-generated method stub
		return interfacesRepository.findByRicefnum(ricefnum);
	}

	@Override
	public List<Interfaces> findBySource(String source) {
		// TODO Auto-generated method stub
		return interfacesRepository.findBySource(source);
	}

	@Override
	public void saveOrUpdateInterfaces(Interfaces i) {
		// TODO Auto-generated method stub
		interfacesRepository.save(i);
	}

}
