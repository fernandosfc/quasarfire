package com.mercadolibre.quasarfire.service;

import com.mercadolibre.quasarfire.entity.Satellite;

public interface SatelliteService {

	Iterable<Satellite> setFleet(Iterable<Satellite> satellites);

	Iterable<Satellite> findAll();
}
