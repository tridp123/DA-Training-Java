package com.springjpa.controller;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.querydsl.core.types.Predicate;
import com.springjpa.dto.LocationDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.model.jpa.QLocation;
import com.springjpa.service.LocationService;
import com.springjpa.service.impl.LocationServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

	public static final Logger log = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	LocationService locationService;

	public LocationController() {
	}

	public LocationController(LocationService locationRepository) {
		super();
		this.locationService = locationRepository;
	}

	/* get */
	// -------------------Retrieve All
	// location--------------------------------------------------------
	@GetMapping(value = "/getalllocationcas", headers = "Accept=application/json")
	public ResponseEntity<List<LocationDTO>> getAllLocationCas() {
		List<LocationDTO> list = convertListLocationCas(locationService.getAllLocations());
		return new ResponseEntity<List<LocationDTO>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/getalllocationjpa", headers = "Accept=application/json")
	public ResponseEntity<List<LocationDTO>> getAllLocationJPA() {
		List<LocationDTO> list = convertListLocationJPA(locationService.getAllLocationInJPA());
		return new ResponseEntity<List<LocationDTO>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/getalllocationjpa/querydsl", headers = "Accept=application/json")
	public ResponseEntity<List<LocationDTO>> getLocationByQueryDslFromJpa(
			@QuerydslPredicate(root = Location.class) Predicate predicate) {

		List<Location> list = locationService.getListLocationByQueryDslFromJpa(predicate);
		List<LocationDTO> dtoList = list.stream().map(product -> convertToDTO(product, DBType.JPA))
				.collect(Collectors.toList());
		return new ResponseEntity<List<LocationDTO>>(dtoList, HttpStatus.OK);
	}

	@GetMapping(value = "/sortbycountry", headers = "Accept=application/json")
	public ResponseEntity<List<LocationDTO>> getLocationSortByCountry() {
		List<Location> list = locationService.getListLocationSortByCountry();
		List<LocationDTO> dtoList = list.stream().map(product -> convertToDTO(product, DBType.JPA))
				.collect(Collectors.toList());
		return new ResponseEntity<List<LocationDTO>>(dtoList, HttpStatus.OK);
	}

	// -------------------Retrieve Single location by id
	@GetMapping(value = "/getlocationcas/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> getLocationCas(@PathVariable("locationId") String id) {
		System.out.println("Fetching location with id " + id);
		LocationCas locationCas = locationService.findByIdInCas(UUID.fromString(id));
		if (locationCas == null) {
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<LocationDTO>(convertToDTO(locationCas, DBType.CASSANDRA), HttpStatus.OK);
	}

	@GetMapping(value = "/getlocationjpa/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> getLocationJPA(@PathVariable("locationId") String id) {
		System.out.println("Fetching location with locationId " + id);
		LocationDTO locationDTO = convertToDTO(locationService.findByIdInJPA(UUID.fromString(id)), DBType.JPA);
		if (locationDTO == null) {
			System.out.println("Location with id " + locationDTO.getLocationId() + " not found");
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("location DTO id: " + locationDTO.getLocationId());
		return new ResponseEntity<LocationDTO>(locationDTO, HttpStatus.OK);
	}
	//use querydsl
	@GetMapping(value = "/getjpa", params = "id")
	public ResponseEntity<LocationDTO> getLocationByIdFromJpa(@RequestParam("id") String id) {
		QLocation qp = QLocation.location;
		Predicate predicate = qp.location_id.eq(UUID.fromString(id));
		Location result = locationService.getOneLocationByQueryDslFromJpa(predicate);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "http://localhost:8080/location/getjpa?id=" + result.getLocation_id());
		return new ResponseEntity<LocationDTO>(convertToDTO(result, DBType.JPA), headers, HttpStatus.OK);
	}

	/* ADD */
	// add location info Cas
	@PostMapping(value = "/addlocation/addcas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> createLocation(@RequestParam String country, @RequestParam String city) {
		LocationCas cas = new LocationCas(UUID.randomUUID(), country, city, DataTimeUtil.getCurrent(),
				DataTimeUtil.getCurrent());

		if (locationService.isExistsLocation(cas)) {
			System.out.println("A location with name " + cas.getCountry() + " already exist");
			return new ResponseEntity<LocationDTO>(HttpStatus.CONFLICT);
		}
		LocationCas a = locationService.saveLocationCas(cas);
		UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponents.path("/location/getlocationcas/{locationId}").buildAndExpand(a.getLocationId()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(a, DBType.CASSANDRA), HttpStatus.CREATED);
	}

	// add location info JPA
	@PostMapping(value = "/addlocation/addjpa", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> addLocationInJPA(@RequestParam String country, @RequestParam String city) {

		Location loc = new Location(UUID.randomUUID(), country, city,
				new Timestamp(DataTimeUtil.getCurrent().getMillis()),
				new Timestamp(DataTimeUtil.getCurrent().getMillis()));

		if (locationService.isExistsLocationinJPA(loc)) {
			System.out.println("A location with name " + loc.getCountry() + " already exist");
			return new ResponseEntity<Location>(HttpStatus.CONFLICT);
		}
		Location a = locationService.saveLocationJPA(loc);

		UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponents.path("/location/getlocationjpa/{locationId}").buildAndExpand(a.getLocation_id()).toUri());
		return new ResponseEntity<Location>(a, headers, HttpStatus.CREATED);
	}

	// add all location from CAS into JPA
	@PostMapping(value = "/addalllocation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocationDTO>> saveAllCasIntoJPA() {
		for (LocationDTO dto : convertListLocationCas(locationService.getAllLocations())) {
			Location tmp = convertToJPAEntity(dto);
			if (!locationService.isExistsLocationinJPA(tmp)) {
				locationService.saveLocationJPA(tmp);
			}
		}
		return new ResponseEntity<List<LocationDTO>>(convertListLocationJPA(locationService.getAllLocationInJPA()),
				HttpStatus.CREATED);
	}

	// add location info Cas
	@PostMapping(value = "/addlocationcas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationCas> addLocationCas(@RequestBody LocationDTO create) {
		LocationCas location = new LocationCas();
		location = convertToCassandraEntity(create);
		locationService.saveLocationCas(location);
		URI uri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/location/getlocationcas/{locationId}")
				.build().expand(location.getLocationId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);
		ResponseEntity<LocationCas> entity = new ResponseEntity<LocationCas>(location, headers, HttpStatus.CREATED);
		return entity;
	}

	// add location info jpa
	@PostMapping(value = "/addlocationjpa", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> addLocationJPA(@RequestBody LocationDTO create) {
		Location location = new Location();
		location = convertToJPAEntity(create);
		locationService.saveLocationJPA(location);
		URI uri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/location/getlocationcas/{locationId}")
				.build().expand(location.getLocation_id()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);
		ResponseEntity<LocationDTO> entity = new ResponseEntity<LocationDTO>(convertToDTO(location, DBType.JPA),
				headers, HttpStatus.CREATED);
		return entity;
	}

	/* UPDATE */
	// update cas
	@PutMapping(value = "/updateincas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> updateLocationInCas(@RequestBody LocationDTO value) {

		LocationCas loc = locationService.findByIdInCas(value.getLocationId());
		loc.setCountry(value.getCountry());
		loc.setCity(value.getCity());
		loc.setCreatedAt(value.getCreatedAt());
		loc.setModifiedAt(DataTimeUtil.getCurrent());

		locationService.updateLocationInCas(loc);

		URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
				.path("/location/getlocationcas/{locationId}").build().expand(loc.getLocationId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		ResponseEntity<LocationDTO> entity = new ResponseEntity<LocationDTO>(convertToDTO(loc, DBType.CASSANDRA),
				headers, HttpStatus.OK);

		return entity;
	}

	// update JPA
	@PutMapping(value = "/updateinjpa", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> updateLocation(@RequestBody LocationDTO location) {

		Location loc = locationService.findByIdInJPA(location.getLocationId());
		loc.setCountry(location.getCountry());
		loc.setCity(location.getCity());
		loc.setCreatedAt(new Timestamp(location.getCreatedAt().getMillis()));
		loc.setModifiedAt(new Timestamp(DataTimeUtil.getCurrent().getMillis()));

		locationService.updateLocationInJPA(loc);

		UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.path("/location/getlocationjpa/{locationId}")
				.buildAndExpand(loc.getLocation_id()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(loc, DBType.JPA), headers, HttpStatus.OK);
	}

	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<LocationDTO> updateLocationQueryDsl(@RequestParam("id") String id,
			@RequestParam("country") String country, @RequestParam("city") String city) {
		QLocation qp = QLocation.location;
		Predicate predicate = qp.location_id.eq(UUID.fromString(id));
		Location result = locationService.getOneLocationByQueryDslFromJpa(predicate);
		if (result == null) {
			return new ResponseEntity<LocationDTO>(HttpStatus.CONFLICT);
		}
		locationService.updateLocationQueryDsl(result, country, city);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "http://localhost:8080/location/getjpa?id=" + result.getLocation_id());
		return new ResponseEntity<LocationDTO>(convertToDTO(result, DBType.JPA), headers, HttpStatus.OK);
	}

	// delete all location in Cas
	@DeleteMapping(value = "deleteall", headers = "Accept=application/json")
	public ResponseEntity<LocationDTO> deleteAllLocation() {
		locationService.deleteAllLocationInCas();
		return new ResponseEntity<LocationDTO>(HttpStatus.NO_CONTENT);
	}

	// delete by id in cas
	@DeleteMapping(value = "/deletecas/{locationId}")
	public ResponseEntity<LocationDTO> deletebyID(@PathVariable("locationId") String id) {
		locationService.deleteLocationById(UUID.fromString(id));
		return new ResponseEntity<LocationDTO>(HttpStatus.NO_CONTENT);
	}

	// delete by id jpa
	@DeleteMapping(value = "/deletejpa")
	@Transactional
	public ResponseEntity<LocationDTO> deleteByIdJPA(@RequestParam(value = "id") String id) {

		UUID locationID = UUID.fromString(id);
		QLocation qp = QLocation.location;
		Predicate predicate = qp.location_id.eq(locationID);

		if (locationService.isExistsLocationJPA(predicate)) {
			locationService.deleteLocationJPA(locationID);
			return new ResponseEntity<LocationDTO>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<LocationDTO>(HttpStatus.FOUND);
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
		List<LocationCas> list = new ArrayList<>();
		list.add(l1);
		list.add(l2);
		list.add(l3);

		for (LocationCas locationCas : list) {
			if (!locationService.isExistsLocation(locationCas)) {
				locationService.saveLocationCas(locationCas);
			}
		}
		return "Done";
	}

}
