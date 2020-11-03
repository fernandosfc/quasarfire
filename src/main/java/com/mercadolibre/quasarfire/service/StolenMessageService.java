package com.mercadolibre.quasarfire.service;

import com.mercadolibre.quasarfire.entity.StolenMessage;

public interface StolenMessageService {

	StolenMessage save(StolenMessage stolenMessage);

	Iterable<StolenMessage> findAll();

	Boolean deleteAll();

}
