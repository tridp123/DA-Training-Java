package com.springjpa.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

	/* get */
	// -------------------Retrieve All location--------------------------------------------------------
	@GetMapping(value = "/getalllocationcas", headers = "Accept=application/json")
	public ResponseEntity<List<LocationDTO>> getAllLocationCas() {
		List<LocationDTO> list = convertListLocationCas(locationRepository.getAllLocations());
		return new ResponseEntity<List<LocationDTO>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/getalllocationjpa", headers = "Accept=application/json")
	public ResponseEntity<List<LocationDTO>> getAllLocationJPA() {
		List<LocationDTO> list = convertListLocationJPA(locationRepository.getAllLocationInJPA());
		return new ResponseEntity<List<LocationDTO>>(list, HttpStatus.OK);
	}

	// -------------------Retrieve Single location by
	// Country--------------------------------------------------------
	@GetMapping(value = "/getlocationcas/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> getLocationCas(@PathVariable("locationId") String id) {
		System.out.println("Fetching location with id " + id);
		LocationCas locationCas = locationRepository.findByIdInCas(UUID.fromString(id));
		if (locationCas==null) {
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<LocationDTO>(convertToDTO(locationCas, DBType.CASSANDRA), HttpStatus.OK);
	}

	@GetMapping(value = "/getlocationjpa/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> getLocationJPA(@PathVariable("locationId") String id) {
		System.out.println("Fetching location with locationId " + id);
		LocationDTO locationDTO = convertToDTO(locationRepository.findByIdInJPA(UUID.fromString(id)), DBType.JPA);
		if (locationDTO == null) {
			System.out.println("Location with id " + locationDTO.getLocationId() + " not found");
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("location DTO id: " + locationDTO.getLocationId());
		return new ResponseEntity<LocationDTO>(locationDTO, HttpStatus.OK);
	}

	// add location info Cas
	@PostMapping(value = "/addlocation/addcas")
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
		headers.setLocation(
				ucBuilder.path("/location/getlocationcas/{locationId}").buildAndExpand(a.getLocationId()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(a, DBType.CASSANDRA), headers, HttpStatus.CREATED);
	}

	// add location info JPA
	@PostMapping(value = "/addlocation/addjpa")
	public ResponseEntity<LocationDTO> addLocationInJPA(@RequestParam String country, @RequestParam String city,
			UriComponentsBuilder ucBuilder) {
		Location loc = new Location(UUID.randomUUID(), country, city,
				new Timestamp(DataTimeUtil.getCurrent().getMillis()),
				new Timestamp(DataTimeUtil.getCurrent().getMillis()));

		if (locationRepository.isExistsLocationinJPA(loc)) {
			System.out.println("A location with name " + loc.getCountry() + " already exist");
			return new ResponseEntity<LocationDTO>(HttpStatus.CONFLICT);
		}
		Location a = locationRepository.saveLocationJPA(loc);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/location/getlocationjpa/{locationId}").buildAndExpand(a.getLocation_id()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(a, DBType.JPA), headers, HttpStatus.CREATED);
	}
	
	//add all location from CAS into JPA
	@PostMapping(value = "/addalllocation")
	public ResponseEntity<List<Location>> saveAllCasIntoJPA(){
		for (LocationDTO dto : convertListLocationCas(locationRepository.getAllLocations())) {
			locationRepository.saveLocationJPA(convertToJPAEntity(dto));
		}
		return new ResponseEntity<List<Location>>(locationRepository.getAllLocationInJPA(), HttpStatus.CREATED);
	}

	// update JPA
	@PutMapping(value = "/updateinjpa", headers = "Accept=application/json")
	public ResponseEntity<LocationDTO> updateLocation( @RequestParam String id, @RequestParam String country,
			@RequestParam String city, UriComponentsBuilder ucBuilder) {
		Location loc = locationRepository.findByIdInJPA(UUID.fromString(id));
		if (!locationRepository.isExistsLocationinJPA(loc)) {
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		locationRepository.updateLocationInJPA(loc, country, city);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/location/getlocationjpa/{locationId}").buildAndExpand(loc.getLocation_id()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(loc, DBType.JPA), headers, HttpStatus.OK);
	}

	// delete all location in Cas
	@DeleteMapping(value = "deleteall", headers = "Accept=application/json")
	public ResponseEntity<LocationDTO> deleteAllLocation() {
		locationRepository.deleteAllLocationInCas();
		return new ResponseEntity<LocationDTO>(HttpStatus.NO_CONTENT);
	}
	//
	@DeleteMapping(value = "/delete/{locationId}")
	public ResponseEntity<LocationDTO>  deleteUser(@PathVariable("locationId") String id) {
		locationRepository.deleteLocationById(UUID.fromString(id));
		return new ResponseEntity<LocationDTO>(HttpStatus.NO_CONTENT);
	}

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

	public List<LocationDTO> convertListLocationCas(List<LocationCas> list) {
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

	public List<LocationDTO> convertListLocationJPA(List<Location> list) {
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
	
	@RequestMapping("/initiallocation")
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
