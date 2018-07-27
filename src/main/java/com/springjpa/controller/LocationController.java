package com.springjpa.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springjpa.dto.LocationDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.service.LocationService;
import com.springjpa.service.impl.LocationServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

	public static final Logger log = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	LocationService locationRepository = new LocationServiceImpl();

	// -------------------Retrieve All
	// location--------------------------------------------------------
	@GetMapping(value = "/getalllocation", headers = "Accept=application/json")
	public ResponseEntity<List<LocationDTO>> getAllLocations() {
		List<LocationDTO> list = convertListLocationCas(locationRepository.getAllLocations());
		return new ResponseEntity<List<LocationDTO>>(list, HttpStatus.OK);
	}

	// -------------------Retrieve Single location by
	// Country--------------------------------------------------------
	@RequestMapping(value = "/getlocation/{country}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> getLocation(@PathVariable("country") String country) {
		System.out.println("Fetching location with country " + country);
		LocationDTO locationDTO = convertToDTO(locationRepository.findByCountry(country), DBType.CASSANDRA);

		if (locationDTO == null) {
			System.out.println("Location with id " + locationDTO.getLocationId() + " not found");
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("location DTO return: " + locationDTO.getCity());
		return new ResponseEntity<LocationDTO>(locationDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addlocation/add", method = RequestMethod.POST)
	public ResponseEntity<LocationDTO> createLocation(@RequestParam String country, @RequestParam String city,
			UriComponentsBuilder ucBuilder) {
		LocationCas cas = new LocationCas(UUID.randomUUID(), country, city, DataTimeUtil.getCurrent(),
				DataTimeUtil.getCurrent());

		if (locationRepository.isExistsLocation(cas)) {
			System.out.println("A location with name " + cas.getCountry() + " already exist");
			return new ResponseEntity<LocationDTO>(HttpStatus.CONFLICT);
		}
		LocationCas a = locationRepository.saveLocationCas(cas);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/location/getlocation/{country}").buildAndExpand(a.getCountry()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(a, DBType.CASSANDRA), headers, HttpStatus.CREATED);
	}

//	@PutMapping(value = "/update", headers = "Accept=application/json")
//	public ResponseEntity<LocationDTO> updateLocation(@RequestBody LocationDTO location) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Location", "http://localhost:8080/location?id=" + location.getLocationId());
//		return new ResponseEntity<LocationDTO>(
//				convertToDTO(locationRepository.updateLocation(convertToJPAEntity(location)), DBType.JPA), headers,
//				HttpStatus.OK);
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

	public List<LocationDTO> convertListLocationCas(Iterable<LocationCas> list) {
		List<LocationDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found location");
		}
		for (LocationCas lo : list) {
			LocationDTO dto = convertToDTO(lo, DBType.CASSANDRA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
	}

	public List<LocationDTO> convertListLocationJPA(Iterable<Location> list) {
		List<LocationDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found location");
		}
		for (Location lo : list) {
			LocationDTO dto = convertToDTO(lo, DBType.JPA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
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
