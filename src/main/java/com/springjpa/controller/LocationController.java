package com.springjpa.controller;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
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

	public LocationController() {
	}

	public LocationController(LocationService locationRepository) {
		super();
		this.locationRepository = locationRepository;
	}

	/* get */
	// -------------------Retrieve All
	// location--------------------------------------------------------
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
		if (locationCas == null) {
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
	@PostMapping(value = "/addlocation/addcas", produces = MediaType.APPLICATION_JSON_VALUE)
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
		return new ResponseEntity<LocationDTO>(convertToDTO(a, DBType.CASSANDRA), HttpStatus.CREATED);
	}

	// add location info JPA
	@PostMapping(value = "/addlocation/addjpa", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> addLocationInJPA(@RequestParam String country, @RequestParam String city) {
		Location loc = new Location(UUID.randomUUID(), country, city,
				new Timestamp(DataTimeUtil.getCurrent().getMillis()),
				new Timestamp(DataTimeUtil.getCurrent().getMillis()));

		if (locationRepository.isExistsLocationinJPA(loc)) {
			System.out.println("A location with name " + loc.getCountry() + " already exist");
			return new ResponseEntity<Location>(HttpStatus.CONFLICT);
		}
		Location a = locationRepository.saveLocationJPA(loc);

		UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponents.path("/location/getlocationjpa/{locationId}").buildAndExpand(a.getLocation_id()).toUri());
		return new ResponseEntity<Location>(a, headers, HttpStatus.CREATED);
	}

	// add all location from CAS into JPA
	@PostMapping(value = "/addalllocation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Location>> saveAllCasIntoJPA() {
		for (LocationDTO dto : convertListLocationCas(locationRepository.getAllLocations())) {
			locationRepository.saveLocationJPA(convertToJPAEntity(dto));
		}
		return new ResponseEntity<List<Location>>(locationRepository.getAllLocationInJPA(), HttpStatus.CREATED);
	}

	// add location info Cas
	@PostMapping(value = "/addlocationcas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationCas> addProduct(@RequestBody LocationDTO create) {
		LocationCas product = new LocationCas();
		product = convertToCassandraEntity(create);
		locationRepository.saveLocationCas(product);
		URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
				.path("/location/getlocationcas/{locationId}").build().expand(product.getLocationId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		ResponseEntity<LocationCas> entity = new ResponseEntity<LocationCas>(product, headers, HttpStatus.CREATED);
		return entity;
	}

	// update cas
	@PutMapping(value = "/updateincas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> updateLocationInCas(@RequestBody LocationDTO value) {
		
		LocationCas loc = new LocationCas();
		loc = convertToCassandraEntity(value);
		locationRepository.updateLocationInCas(loc);
		
		URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
				.path("/location/getlocationcas/{locationId}").build().expand(loc.getLocationId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		ResponseEntity<LocationDTO> entity = new ResponseEntity<LocationDTO>(convertToDTO(loc, DBType.CASSANDRA), headers, HttpStatus.CREATED);
		
		return entity;
	}

	// update JPA
	@PutMapping(value = "/updateinjpa", headers = "Accept=application/json")
	public ResponseEntity<LocationDTO> updateLocation(@RequestBody LocationDTO location) {
		Location loc = convertToJPAEntity(location);
		if (!locationRepository.isExistsLocationinJPA(loc)) {
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		locationRepository.updateLocationInJPA(loc);

		UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.path("/location/getlocationjpa/{locationId}")
				.buildAndExpand(loc.getLocation_id()).toUri());
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
	public ResponseEntity<LocationDTO> deleteUser(@PathVariable("locationId") String id) {
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
//			dto.getCreatedAt().withZone(DateTimeZone.UTC);
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

	public boolean compareLocation(LocationCas cas, LocationCas cas2) {
		return cas.getLocationId().equals(cas2.getLocationId());
	}

	@RequestMapping("/initiallocation")
	public String process() {
		// save a Location info Cassandra
		// sample data
		LocationCas l1 = new LocationCas(UUID.randomUUID(), "USA", "New York", DataTimeUtil.getCurrent(),
				DataTimeUtil.getCurrent());
		LocationCas l2 = new LocationCas(UUID.randomUUID(), "Japan", "Tokyo", DataTimeUtil.getCurrent(),
				DataTimeUtil.getCurrent());
		LocationCas l3 = new LocationCas(UUID.randomUUID(), "Laos", "Vieng Chan", DataTimeUtil.getCurrent(),
				DataTimeUtil.getCurrent());
		locationRepository.saveLocationCas(l1);
		locationRepository.saveLocationCas(l2);
		locationRepository.saveLocationCas(l3);

		return "Done";
	}

}
