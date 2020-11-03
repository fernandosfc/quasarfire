package com.mercadolibre.quasarfire.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import com.mercadolibre.quasarfire.config.ApplicationLogger;
import com.mercadolibre.quasarfire.entity.Satellite;
import com.mercadolibre.quasarfire.repo.SatelliteRepository;
import com.mercadolibre.quasarfire.service.SatelliteService;
import com.mercadolibre.quasarfire.service.StolenMessageService;

@Service
public class SatelliteServiceImpl implements SatelliteService {

	@Autowired
	SatelliteRepository satelliteRepository;
	
	@Autowired
	private StolenMessageService stolenMessageService;
	
	@Autowired
	private ApplicationLogger applicationLogger;
	
	@Override
	@Transactional
	public Iterable<Satellite> setFleet(Iterable<Satellite> satellites) throws TransactionSystemException {
		stolenMessageService.deleteAll();
		satelliteRepository.deleteAll();
		return satelliteRepository.saveAll(satellites);
	}

	@Override
	public Iterable<Satellite> findAll() {
		Iterable<Satellite> result = null;
		try {
			result = satelliteRepository.findAll();
		} catch (RuntimeException ex) {
			applicationLogger.LogError("SatelliteServiceImpl.findAll_errorRetrieving");
			applicationLogger.LogError(ex);
		}
		return result;
	}

}
