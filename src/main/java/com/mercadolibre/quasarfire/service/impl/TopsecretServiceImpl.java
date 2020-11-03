package com.mercadolibre.quasarfire.service.impl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.quasarfire.config.ApplicationLogger;
import com.mercadolibre.quasarfire.entity.OriginalMessage;
import com.mercadolibre.quasarfire.entity.Satellite;
import com.mercadolibre.quasarfire.entity.StolenMessage;
import com.mercadolibre.quasarfire.service.StolenMessageService;
import com.mercadolibre.quasarfire.service.TopsecretService;
import com.mercadolibre.quasarfire.util.MessageProcessor;

@Service
public class TopsecretServiceImpl implements TopsecretService {

	@Autowired
	private StolenMessageService stolenMessageService;
	
	@Autowired
	MessageProcessor messageProcessor;
	
	@Autowired
	private ApplicationLogger applicationLogger;
	
	@Override
	public OriginalMessage getOriginalMessage() {
		
		List<Satellite> satellites = this.getAvailableSatellites();
		Iterable<StolenMessage> storedStolenMessages = stolenMessageService.findAll();
		List<StolenMessage> list = new ArrayList<StolenMessage>();
		storedStolenMessages.forEach(list::add);
		
		Point2D.Float targetPosition = messageProcessor.calculateTargetPosition(satellites, storedStolenMessages);
		if (targetPosition == null) {
			applicationLogger.LogInfo("TopsecretServiceImpl.getOriginalMessage_targetNotFound");
			return null;
		}

		String[] message = messageProcessor.resolveMessage(list, satellites.size());
		if (message == null) {
			applicationLogger.LogInfo("TopsecretServiceImpl.getOriginalMessage_messageMissing");
			return null;
		}
		
		OriginalMessage result = new OriginalMessage();
		result.setPosition(targetPosition);
		result.setMessage(Arrays.toString(message));
		return result;
	}

	@Override
	public Boolean saveStolenMessage(StolenMessage stolenMessage) {
		return stolenMessageService.save(stolenMessage) != null;
	}
	
	private List<Satellite> getAvailableSatellites(){
		Satellite kenobi = new Satellite();
		kenobi.setName("kenobi");
		kenobi.setPosition(new Point2D.Float(3, 11));
		
		Satellite skywalker = new Satellite();
		skywalker.setName("skywalker");
		skywalker.setPosition(new Point2D.Float(4, 4));
		
		Satellite sato = new Satellite();
		sato.setName("sato");
		sato.setPosition(new Point2D.Float(9, 5));
		
		List<Satellite> satellites = new ArrayList<Satellite>();
		satellites.add(kenobi);
		satellites.add(skywalker);
		satellites.add(sato);
		
		return satellites;
	}

}
