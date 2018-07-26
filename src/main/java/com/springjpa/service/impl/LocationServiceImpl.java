package com.springjpa.service.impl;


import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

//import com.querydsl.core.types.Predicate;
import com.springjpa.dto.LocationDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.repository.LocationCasRepository;
import com.springjpa.repository.LocationRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.LocationService;

@Service
public class LocationServiceImpl extends BaseService implements LocationService {

	@Autowired
	LocationCasRepository locationRepository;
	
	@Autowired
	LocationRepository jpaRepository;
	
	public LocationServiceImpl() {
	}
	
	@Override
	public Iterable<LocationCas> getAllLocations() {
		return locationRepository.findAll();
	}

	@Override
	public LocationCas saveLocationCas(LocationCas locationCas) {
		return locationRepository.save(locationCas);
	}

	@Override
	public Location saveLocationJPA(Location location) {
		return jpaRepository.save(location);
	}

//	@Override
//	@Transactional(readOnly = true)
//	public Location getOneLocationByQueryDslFromJpa(Predicate predicate) {
//		Location result = jpaRepository.findOne(predicate);
//		if (result==null) {
//			throw new NoDataFoundException("Not found data in DB");
//		}
//		return result;
//	}
//
//	@Override
//	@Transactional(readOnly = true)
//	public List<Location> getLocationByQueryDslFromJpa(Predicate predicate) {
//		List<Location> list = new ArrayList<>();
//		jpaRepository.findAll(predicate).forEach(list::add);
//		return list;
//	}
	public LocationDTO convertToDTO(Object obj, DBType type) {
		LocationDTO dto = null;
		if (obj == null) {
			throw new NoDataFoundException("Not found location");
		}
		if (type == DBType.JPA) {
			Location location = (Location) obj;
			dto = new LocationDTO(location.getLocation_id(), location.getCountry(), location.getCity(), new DateTime(location.getCreatedAt()), new DateTime(location.getModifiedAt()));
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
		Location location = new Location(dto.getLocationId(), dto.getCountry(), dto.getCity(), new Timestamp(dto.getCreatedAt().getMillis()),new Timestamp(dto.getModifiedAt().getMillis()));
		return location;
	}


	public LocationCas convertToCassandraEntity(LocationDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		LocationCas location = new LocationCas(dto.getLocationId(), dto.getCountry(), dto.getCity(),
				dto.getCreatedAt(), dto.getModifiedAt());
		return location;
	}
}
