package com.mercadolibre.quasarfire;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import com.mercadolibre.quasarfire.entity.Satellite;
import com.mercadolibre.quasarfire.entity.StolenMessage;
import com.mercadolibre.quasarfire.service.SatelliteService;
import com.mercadolibre.quasarfire.service.StolenMessageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SetFleetTest {
	
	@Autowired
	SatelliteService satelliteService;
	
	@Autowired
	StolenMessageService stolenMessageService;
	
	@Test
	public void setFleetTest1() throws Exception {
		Assertions.assertNotNull(satelliteService);
		
		//Create satellites
		List<Satellite> satellites = getNewSatellites();
		satelliteService.setFleet(satellites);
		
		//Validate that they were created
		Iterable<Satellite> storedSatellites = satelliteService.findAll();
		this.validateCollection(storedSatellites, satellites);
		
		//Create stolen messages
		List<StolenMessage> stolenMessages = getNewStolenMessages();
		stolenMessages.forEach(stolenMessageService::save);
		
		//Validate that they were created
		Iterable<StolenMessage> storedStolenMessages = stolenMessageService.findAll();
		this.validateCollection(storedStolenMessages, stolenMessages);

		//Create new fleet of satellites
		List<Satellite> satellites2 = getNewSatellites2();
		satelliteService.setFleet(satellites2);
		
		//Validate that new fleet was created overwriting first one and that stored messages were removed
		Iterable<Satellite> storedSatellites2 = satelliteService.findAll();
		this.validateCollection(storedSatellites2, satellites2);
		Assertions.assertEquals(stolenMessageService.findAll().spliterator().getExactSizeIfKnown(), 0);
		
		//Attempt to create new fleet with invalid information
		List<Satellite> invalidSatellites = getInvalidSatellites();
		RuntimeException expected = null;
		try {
			satelliteService.setFleet(invalidSatellites);
		} catch (TransactionSystemException ex) {
			expected = ex;
		}
		Assertions.assertNotNull(expected);
		
		//Validate that new fleet was not created and that pre-existing data remain unmodified due to transaction rollback
		Iterable<Satellite> storedSatellites3 = satelliteService.findAll();
		this.validateCollection(storedSatellites3, satellites2);
	}
	
	private <T> void validateCollection(Iterable<T> stored, List<T> expected) {
		List<T> storedList = new ArrayList<T>();
		stored.forEach(storedList::add);
		Assertions.assertEquals(expected.stream().filter(storedList::contains).count(), expected.size());
		Assertions.assertEquals(storedList.size(), expected.size());
	}
	
	
	private List<StolenMessage> getNewStolenMessages(){
		StolenMessage kenobi = new StolenMessage();
		kenobi.setName("kenobi");
		String[] kmsgs = {"", "este", "es", "un", "mensaje"};
		kenobi.setDistance((float) 1);
		kenobi.setMessage(kmsgs);
		
		StolenMessage skywalker = new StolenMessage();
		skywalker.setName("skywalker");
		String[] skmsgs = {"este", "", "un", "mensaje"};
		skywalker.setMessage(skmsgs);
		skywalker.setDistance((float) 1);
		
		StolenMessage sato = new StolenMessage();
		sato.setName("sato");
		String[] smsgs = {"", "", "es", "", "mensaje"};
		sato.setMessage(smsgs);
		sato.setDistance((float) 1);
		
		List<StolenMessage> satellites = new ArrayList<StolenMessage>();
		satellites.add(kenobi);
		satellites.add(skywalker);
		satellites.add(sato);
		
		return satellites;
	}
	private List<Satellite> getNewSatellites(){
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
	private List<Satellite> getNewSatellites2(){
		Satellite kenobi = new Satellite();
		kenobi.setName("kenobi2");
		kenobi.setPosition(new Point2D.Float(3, 11));
		
		Satellite skywalker = new Satellite();
		skywalker.setName("skywalker2");
		skywalker.setPosition(new Point2D.Float(4, 4));
		
		Satellite sato = new Satellite();
		sato.setName("sato2");
		sato.setPosition(new Point2D.Float(9, 5));
		
		List<Satellite> satellites = new ArrayList<Satellite>();
		satellites.add(kenobi);
		satellites.add(skywalker);
		satellites.add(sato);
		
		return satellites;
	}
	private List<Satellite> getInvalidSatellites(){
		Satellite kenobi = new Satellite();
		kenobi.setName("kenobi3");
		kenobi.setPosition(new Point2D.Float(3, 11));
		
		Satellite skywalker = new Satellite();
		skywalker.setName("skywalker3");
		
		Satellite sato = new Satellite();
		sato.setName("sato3");
		sato.setPosition(new Point2D.Float(9, 5));
		
		List<Satellite> satellites = new ArrayList<Satellite>();
		satellites.add(kenobi);
		satellites.add(skywalker);
		satellites.add(sato);
		
		return satellites;
	}
}
	
