package com.mercadolibre.quasarfire.repo;

import org.springframework.stereotype.Repository;

import com.mercadolibre.quasarfire.entity.Satellite;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface SatelliteRepository extends CrudRepository<Satellite, Integer> {

}
