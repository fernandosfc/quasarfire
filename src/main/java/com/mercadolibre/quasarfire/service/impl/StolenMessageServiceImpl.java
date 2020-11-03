package com.mercadolibre.quasarfire.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.quasarfire.config.ApplicationLogger;
import com.mercadolibre.quasarfire.entity.StolenMessage;
import com.mercadolibre.quasarfire.repo.StolenMessageRepository;
import com.mercadolibre.quasarfire.service.StolenMessageService;

@Service
public class StolenMessageServiceImpl implements StolenMessageService {

	@Autowired
	private StolenMessageRepository stolenMessageRepository;

	@Autowired
	private ApplicationLogger applicationLogger;

	@Override
	public StolenMessage save(StolenMessage stolenMessage) {
		StolenMessage result = null;
		try {
			result = stolenMessageRepository.save(stolenMessage);
		} catch (RuntimeException ex) {
			applicationLogger.LogError("StolenMessageServiceImpl.save_errorOnSave", stolenMessage.getName());
			applicationLogger.LogError(ex);
		}
		return result;
	}

	@Override
	public Iterable<StolenMessage> findAll() {
		Iterable<StolenMessage> result = null;
		try {
			result = stolenMessageRepository.findAll();
		} catch (RuntimeException ex) {
			applicationLogger.LogError("StolenMessageServiceImpl.findAll_errorRetrieving");
			applicationLogger.LogError(ex);
		}
		return result;
	}

	@Override
	public Boolean deleteAll() {
		Boolean result = false;
		try {
			stolenMessageRepository.deleteAll();
			result = true;
		} catch (RuntimeException ex) {
			applicationLogger.LogError("StolenMessageServiceImpl.deleteAll_errorDeleting");
			applicationLogger.LogError(ex);
		}
		return result;
	}

}
