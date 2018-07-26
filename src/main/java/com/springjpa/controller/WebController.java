package com.springjpa.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springjpa.dto.LocationDTO;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
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
		// save a single Customer
		DateTime dt = new DateTime(DateTimeZone.forID("UTC"));
		Timestamp ts = new Timestamp(dt.getMillis());

//		repository.save(new Customer("Jack", "Smith", ts));

		// save a Location info Cassandra
//		locationRepository
//				.save(new LocationCas(UUID.randomUUID(), "VietNamese", "Ho Chi Minh", DateTime.now(), DateTime.now()));

		Location result = locationRepository.saveLocationJPA(new Location(UUID.randomUUID(), "Korea", "Gamnam", ts, ts));
		System.out.println("location da luu:" + result.toString());

		// save a list of Customers
//		repository.save(Arrays.asList(new Customer("Adam", "Johnson"), new Customer("Kim", "Smith"),
//				new Customer("David", "Williams"), new Customer("Peter", "Davis")));

		return "Done";
	}

	@RequestMapping("/savelocation")
	public String saveLocation() {
		System.out.print("location save:");
		for (LocationCas lo : locationRepository.getAllLocations()) {
			
			System.out.print("location test: " + lo.toString());

			LocationDTO dto = locationRepository.convertToDTO(lo, DBType.CASSANDRA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			System.out.print("location dto: " + dto.toString());
			
			Location add = locationRepository.convertToJPAEntity(dto);
			Location result = locationRepository.saveLocationJPA(add);
			System.out.println(result.toString());
		}

		String result = "" + locationRepository.getAllLocations().toString() + "</br>" + "Done ! ";
		return result;
	}

	@RequestMapping("/findalllocation")
	public String findAllLocation() {
		String result = "";

		for (LocationCas lo : locationRepository.getAllLocations()) {
			result += lo.toString() + "<br>";
		}

		return result;
	}
//
//	@RequestMapping("/findall")
//	public String findAll() {
//		String result = "";
//
//		for (Customer cust : repository.findAll()) {
//			result += cust.toString() + "<br>";
//		}
//
//		return result;
//	}
//
//	@RequestMapping("/findbyid")
//	public String findById(@RequestParam("id") long id) {
//		String result = "";
//		result = repository.findOne(id).toString();
//		return result;
//	}
//
//	@RequestMapping("/findbylastname")
//	public String fetchDataByLastName(@RequestParam("lastname") String lastName) {
//		String result = "";
//
//		for (Customer cust : repository.findByLastName(lastName)) {
//			result += cust.toString() + "<br>";
//		}
//
//		return result;
//	}
}
