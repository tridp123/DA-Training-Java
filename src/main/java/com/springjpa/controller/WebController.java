package com.springjpa.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.service.LocationService;
import com.springjpa.service.impl.LocationServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RestController
public class WebController {

	public static final Logger log = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	LocationService locationRepository = new LocationServiceImpl();

	@RequestMapping("/save")
	public String process() {
		// save a Location info Cassandra
		// sample data
		LocationCas l1 = new LocationCas(UUID.randomUUID(), "USA", "New York", DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		LocationCas l2 = new LocationCas(UUID.randomUUID(), "Japan", "Tokyo", DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		LocationCas l3 = new LocationCas(UUID.randomUUID(), "Laos", "Vieng Chan", DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());

		locationRepository.saveLocationCas(l1);
		locationRepository.saveLocationCas(l2);
		locationRepository.saveLocationCas(l3);

		return "Done";
	}
}
