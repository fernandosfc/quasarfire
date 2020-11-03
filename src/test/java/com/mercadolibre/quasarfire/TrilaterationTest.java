package com.mercadolibre.quasarfire;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.mercadolibre.quasarfire.config.ApplicationLogger;
import com.mercadolibre.quasarfire.entity.Satellite;
import com.mercadolibre.quasarfire.entity.StolenMessage;
import com.mercadolibre.quasarfire.util.MessageProcessor;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TrilaterationTest {
	
	@Mock
    private ApplicationLogger applicationLogger;
	
    @InjectMocks
    private MessageProcessor messageProcessor;
	
	@Test
	public void trilaterationTest1() throws Exception {
		Mockito.doNothing().when(applicationLogger).LogInfo(Mockito.anyString(), Mockito.anyString());
		
		Point2D.Float expectedLocation = new Point2D.Float(7, 7);
		List<Satellite> satellites = this.getAvailableSatellites();
		List<StolenMessage> stolenMessages = this.getStolenMessages(expectedLocation, satellites);
		
		Point2D.Float targetPosition = messageProcessor.calculateTargetPosition(satellites, stolenMessages);
				
		System.out.println("Trilateration function used to calculate target location using satellites location and their distnace to target");
		System.out.println(String.format("Comparing x results: %f vs %f", targetPosition.getX(), expectedLocation.getX()));
		System.out.println(String.format("Comparing y results: %f vs %f", targetPosition.getY(), expectedLocation.getY()));
		assertEquals(targetPosition.getX(), expectedLocation.getX(), 0);
		assertEquals(targetPosition.getY(), expectedLocation.getY(), 0);
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
	
	private List<StolenMessage> getStolenMessages(Float targetLocation, List<Satellite> satellites){
		System.out.println(String.format("Considering target located in [%f,%f]", targetLocation.getX(), targetLocation.getY()));
		List<StolenMessage> result = new ArrayList<StolenMessage>();
		for (Satellite i : satellites) {
			StolenMessage smsg = new StolenMessage();
			smsg.setName(i.getName());
			float distance = (float)this.calculateDistance(targetLocation, i.getPosition());
			smsg.setDistance(distance);
			System.out.println(String.format("Stolen message: %s located in [%f,%f] - Calculated distance: %f",
					i.getName(), i.getPosition().getX(), i.getPosition().getY(), distance));
			result.add(smsg);
		}		
		return result;
	}
	
    private double calculateDistance(Point2D.Float targetLocation, Point2D.Float sourceLocation) {
        double distance = Math.sqrt(
                    Math.pow(targetLocation.getX() - sourceLocation.getX(), 2) +
                            Math.pow(targetLocation.getY() - sourceLocation.getY(), 2));
        return distance;
    }
}
