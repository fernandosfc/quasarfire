package com.mercadolibre.quasarfire;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.mercadolibre.quasarfire.config.ApplicationLogger;
import com.mercadolibre.quasarfire.entity.StolenMessage;
import com.mercadolibre.quasarfire.util.MessageProcessor;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MessageProcessorTest {
	
	@Mock
    private ApplicationLogger applicationLogger;
	
    @InjectMocks
    private MessageProcessor messageProcessor;
    
	@Test
	public void reconstructionTest1() throws Exception {
		Mockito.doNothing().when(applicationLogger).LogInfo(Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(applicationLogger).LogInfo(Mockito.anyString());
		
		int availableSatellites = 3;
		String[] expectedMessage = {"este", "es", "un", "mensaje"};
		List<StolenMessage> stolenMessages = this.getStolenMessages();
		
		String[] result = messageProcessor.resolveMessage(stolenMessages, availableSatellites);
		
		Assertions.assertNotNull(result);
		Assertions.assertArrayEquals(expectedMessage, result);
	}
	
	private List<StolenMessage> getStolenMessages(){
		StolenMessage kenobi = new StolenMessage();
		kenobi.setName("kenobi");
		String[] kmsgs = {"", "este", "es", "un", "mensaje"};
		kenobi.setMessage(kmsgs);
		
		StolenMessage skywalker = new StolenMessage();
		skywalker.setName("skywalker");
		String[] skmsgs = {"este", "", "un", "mensaje"};
		skywalker.setMessage(skmsgs);
		
		StolenMessage sato = new StolenMessage();
		sato.setName("sato");
		String[] smsgs = {"", "", "es", "", "mensaje"};
		sato.setMessage(smsgs);
		
		List<StolenMessage> satellites = new ArrayList<StolenMessage>();
		satellites.add(kenobi);
		satellites.add(skywalker);
		satellites.add(sato);
		
		return satellites;
	}

}
