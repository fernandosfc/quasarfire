package com.mercadolibre.quasarfire.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.quasarfire.entity.OriginalMessage;
import com.mercadolibre.quasarfire.entity.StolenMessage;
import com.mercadolibre.quasarfire.service.TopsecretService;

@RestController
public class TopsecretController {

	@Autowired
	private TopsecretService topsecretService;
	
	@GetMapping("/topsecret_split/")
	public @ResponseBody ResponseEntity<OriginalMessage> topsecret() {
		
		OriginalMessage entity = topsecretService.getOriginalMessage();
		
		if (entity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			return new ResponseEntity<OriginalMessage>(entity, HttpStatus.OK);
		}

	}
	
	@PostMapping("/topsecret_split/{satellite_name}")
	public @ResponseBody ResponseEntity<?> topsecretSplit(
			@RequestBody @Valid StolenMessage stolenMessage,
			@PathVariable("satellite_name") String satelliteName) {
		
		if(stolenMessage == null || satelliteName.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		stolenMessage.setName(satelliteName);
		Boolean result = topsecretService.saveStolenMessage(stolenMessage);
		
		if (result == false) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		
	}
}