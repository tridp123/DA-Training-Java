package com.springjpa.controller;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springjpa.dto.LocationDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.service.LocationService;
import com.springjpa.service.impl.LocationServiceImpl;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

	public static final Logger log = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	LocationService locationRepository = new LocationServiceImpl();

	@GetMapping(value = "/getalllocation", headers = "Accept=application/json")
	public ResponseEntity<LocationCas> getAllLocations() {
		String result = "List location is retrieved from Cassandra DB: " + "</br>";
		for (LocationCas lo : locationRepository.getAllLocations()) {

			LocationDTO dto = locationRepository.convertToDTO(lo, DBType.CASSANDRA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);

			Location add = locationRepository.convertToJPAEntity(dto);
			Location r = locationRepository.saveLocationJPA(add);
			result += r.toString();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "http://localhost:8080/location=" + result);

		return new ResponseEntity<LocationCas>(headers,HttpStatus.OK);
	}

//	@PostMapping(value = "/add", headers = "Accept=application/json")
//	public ResponseEntity<LocationDTO> addLocation(@RequestBody LocationDTO location) {
//		
//		LocationDTO result = convertToDTO(service.addLocation(convertToJPAEntity(location)), DBType.JPA);
//		
//		HttpHeaders headers = new HttpHeaders();
//		
//		//add in response header
//		headers.add("Location", "http://localhost:8080/location?id=" + result.getLocationId());
//		
//		return new ResponseEntity<LocationDTO>(result, headers, HttpStatus.CREATED);
//	}
//
//	@PutMapping(value = "/update", headers = "Accept=application/json")
//	public ResponseEntity<LocationDTO> updateLocation(@RequestBody LocationDTO location) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Location", "http://localhost:8080/location?id=" + location.getLocationId());
//		return new ResponseEntity<LocationDTO>(
//				convertToDTO(service.updateLocation(convertToJPAEntity(location)), DBType.JPA), headers, HttpStatus.OK);
//	}

//	@GetMapping(value = "/jpa/querydsl")
//	public ResponseEntity<List<LocationDTO>> getLocationByQueryDslFromJpa(
//			@QuerydslPredicate(root = Location.class) Predicate predicate) {
//		List<Location> list = service.getLocationByQueryDslFromJpa(predicate);
//		List<LocationDTO> dtoList = list.stream().map(product -> convertToDTO(product, DBType.JPA))
//				.collect(Collectors.toList());
//		return new ResponseEntity<List<LocationDTO>>(dtoList, HttpStatus.OK);
//	}

//	@GetMapping(value = "/jpa", params = "id")
//	public ResponseEntity<LocationDTO> getLocationByIdFromJpa(@RequestParam("id") UUID id) {
//		QLocation qp = QLocation.location;
//		Predicate predicate = qp.locationId.eq(id);
//		Location result = service.getOneLocationByQueryDslFromJpa(predicate);
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Location", "http://localhost:8080/location?id=" + result.getLocationId());
//		return new ResponseEntity<LocationDTO>(convertToDTO(result, DBType.JPA), headers, HttpStatus.OK);
//	}
//
//	@GetMapping(value = "/jpa", params = "country")
//	public ResponseEntity<List<LocationDTO>> getLocationByCountryFromJpa(@RequestParam("country") String country) {
//		QLocation qp = QLocation.location;
//		Predicate predicate = qp.country.eq(country);
//		List<Location> list = service.getLocationByQueryDslFromJpa(predicate);
//		List<LocationDTO> dtoList = list.stream().map(product -> convertToDTO(product, DBType.JPA))
//				.collect(Collectors.toList());
//		return new ResponseEntity<List<LocationDTO>>(dtoList, HttpStatus.OK);
//	}
//
//	@GetMapping(value = "/jpa", params = "city")
//	public ResponseEntity<List<LocationDTO>> getLocationByCityFromJpa(@RequestParam("city") String city) {
//		QLocation qp = QLocation.location;
//		Predicate predicate = qp.city.eq(city);
//		List<Location> list = service.getLocationByQueryDslFromJpa(predicate);
//		List<LocationDTO> dtoList = list.stream().map(product -> convertToDTO(product, DBType.JPA))
//				.collect(Collectors.toList());
//		return new ResponseEntity<List<LocationDTO>>(dtoList, HttpStatus.OK);
//	}

	public LocationDTO convertToDTO(Object obj, DBType type) {
		LocationDTO dto = null;
		if (obj == null) {
			throw new NoDataFoundException("Not found location");
		}
		if (type == DBType.JPA) {
			Location location = (Location) obj;
			dto = new LocationDTO(location.getLocation_id(), location.getCountry(), location.getCity(),
					new DateTime(location.getCreatedAt()), new DateTime(location.getModifiedAt()));
		} else if (type == DBType.CASSANDRA) {
			LocationCas location = (LocationCas) obj;
			dto = new LocationDTO(location.getLocationId(), location.getCountry(), location.getCity(),
					location.getCreatedAt(), location.getModifiedAt());
		} else {
			throw new BadRequestException("No type");
		}
		return dto;
	}

	public Location convertToJPAEntity(LocationDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		Location location = new Location(dto.getLocationId(), dto.getCountry(), dto.getCity(),
				new Timestamp(dto.getCreatedAt().getMillis()), new Timestamp(dto.getModifiedAt().getMillis()));
		return location;
	}

	public LocationCas convertToCassandraEntity(LocationDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		LocationCas location = new LocationCas(dto.getLocationId(), dto.getCountry(), dto.getCity(), dto.getCreatedAt(),
				dto.getModifiedAt());
		return location;
	}

}
