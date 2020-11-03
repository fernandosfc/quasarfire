package com.mercadolibre.quasarfire;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mercadolibre.quasarfire.service.StolenMessageService;
import com.mercadolibre.quasarfire.util.MessageProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AutowireSmokeTest {

	@Autowired
	MessageProcessor messageProcessor;
	
	@Autowired
	StolenMessageService stolenMessageService;
	
	@Test
	public void messageProcessorTest() throws Exception {
		Assertions.assertNotNull(messageProcessor);
	}
	
	@Test
	public void stolenMessageServiceTest() throws Exception {
		Assertions.assertNotNull(stolenMessageService);
	}
}
