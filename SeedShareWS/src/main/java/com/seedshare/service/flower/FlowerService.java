package com.seedshare.service.flower;

import org.springframework.http.ResponseEntity;

import com.seedshare.entity.Flower;
import com.seedshare.service.BasicService;

/**
 * Service interface of Flower
 * 
 * @author joao.silva
 */
public interface FlowerService extends BasicService<Flower, Long> {

	ResponseEntity<?> findAll();
	
}
