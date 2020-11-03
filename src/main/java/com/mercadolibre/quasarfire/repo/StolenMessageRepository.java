package com.mercadolibre.quasarfire.repo;

import org.springframework.stereotype.Repository;

import com.mercadolibre.quasarfire.entity.StolenMessage;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface StolenMessageRepository extends CrudRepository<StolenMessage, Integer> {

}
