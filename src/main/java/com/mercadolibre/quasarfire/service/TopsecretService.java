package com.mercadolibre.quasarfire.service;

import com.mercadolibre.quasarfire.entity.OriginalMessage;
import com.mercadolibre.quasarfire.entity.StolenMessage;

public interface TopsecretService {

	Boolean saveStolenMessage(StolenMessage stolenMessage);

	OriginalMessage getOriginalMessage();

}
