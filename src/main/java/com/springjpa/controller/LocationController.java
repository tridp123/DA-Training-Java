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
import org.springframework.web.bind.annotation.PutMapping;
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
	@RequestMapping(value = "/getlocationcas/{country}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocationDTO>> getLocationCas(@PathVariable("country") String country) {
		System.out.println("Fetching location with country " + country);
		List<LocationCas> locationCas = locationRepository.findByCountryInCas(country);
		List<LocationDTO> list = new ArrayList<>();
		for (LocationCas cas : locationCas) {
			list.add(convertToDTO(cas, DBType.CASSANDRA));
		}
		if (list == null) {
			return new ResponseEntity<List<LocationDTO>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<LocationDTO>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/getlocationjpa/{country}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationDTO> getLocationJPA(@PathVariable("country") String country) {
		System.out.println("Fetching location with country " + country);
		LocationDTO locationDTO = convertToDTO(locationRepository.findByCountryInJPA(country), DBType.JPA);

		if (locationDTO == null) {
			System.out.println("Location with id " + locationDTO.getLocationId() + " not found");
			return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("location DTO CITY: " + locationDTO.getCity());
		return new ResponseEntity<LocationDTO>(locationDTO, HttpStatus.OK);
	}

	// add location info Cas
	@RequestMapping(value = "/addlocation/addcas", method = RequestMethod.POST)
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
				ucBuilder.path("/location/getlocationcas/{country}").buildAndExpand(a.getCountry()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(a, DBType.CASSANDRA), headers, HttpStatus.CREATED);
	}

	// add location info JPA
	@RequestMapping(value = "/addlocation/addjpa", method = RequestMethod.POST)
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
				ucBuilder.path("/location/getlocationjpa/{country}").buildAndExpand(a.getCountry()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(a, DBType.JPA), headers, HttpStatus.CREATED);
	}
	
	//add all location from CAS into JPA
	@RequestMapping(value = "/addalllocation", method = RequestMethod.POST)
	public ResponseEntity<Iterable<Location>> saveAllCasIntoJPA(){
		for (LocationDTO dto : convertListLocationCas(locationRepository.getAllLocations())) {
			locationRepository.saveLocationJPA(convertToJPAEntity(dto));
		}
		return new ResponseEntity<Iterable<Location>>(locationRepository.getAllLocationInJPA(), HttpStatus.CREATED);
	}

	// update JPA
	@PutMapping(value = "/updateinjpa", headers = "Accept=application/json")
	public ResponseEntity<LocationDTO> updateLocation( @RequestParam String country,
			@RequestParam String city, UriComponentsBuilder ucBuilder) {
		Location loc = locationRepository.findByCountryInJPA(country);
		locationRepository.updateLocationInJPA(loc, country, city);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/location/getlocationjpa/{country}").buildAndExpand(loc.getCountry()).toUri());
		return new ResponseEntity<LocationDTO>(convertToDTO(loc, DBType.JPA), headers, HttpStatus.OK);
	}

	// delete all location in Cas
	@DeleteMapping(value = "deleteall", headers = "Accept=application/json")
	public ResponseEntity<LocationDTO> deleteAllLocation() {
		locationRepository.deleteAllLocationInCas();
		return new ResponseEntity<LocationDTO>(HttpStatus.NO_CONTENT);
	}

	//
	@RequestMapping(value = "/delete/{country}", method = RequestMethod.DELETE)
	public ResponseEntity<List<LocationDTO>> deleteUser(@PathVariable("country") String country) {

		List<LocationCas> dto = locationRepository.findByCountryInCas(country);
		if (dto == null) {
			return new ResponseEntity<List<LocationDTO>>(HttpStatus.NOT_FOUND);
		}
		
		locationRepository.deleteLocationByCountry(country);
		return new ResponseEntity<List<LocationDTO>>(HttpStatus.NO_CONTENT);
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