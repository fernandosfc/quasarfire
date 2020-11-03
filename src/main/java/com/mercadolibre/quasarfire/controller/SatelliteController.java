package com.mercadolibre.quasarfire.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.quasarfire.entity.Satellite;
import com.mercadolibre.quasarfire.service.SatelliteService;

@RestController
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;
		
	@PostMapping("/satellite_fleet/")
	public @ResponseBody ResponseEntity<?> setFleet(
			@RequestBody @Valid Iterable<Satellite> satellites) {
		
		if(satellites == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Boolean result = satelliteService.setFleet(satellites) != null;
		
		if (result == false) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		
	}
}