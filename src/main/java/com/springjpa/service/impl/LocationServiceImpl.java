package com.springjpa.service.impl;

import java.sql.Timestamp;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
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
import com.springjpa.util.DataTimeUtil;

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

	@Override
	public Location updateLocation(Location location) {
		if ((jpaRepository.findOne(location.getLocation_id()) == null)) {
			throw new NoDataFoundException("Location ID '" + location.getLocation_id() + "' not found in DB");
		}
		location.setModifiedAt(new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		return jpaRepository.save(location);
	}


	@Override
	public boolean deleteAllLocation() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean isExistsLocation(LocationCas locationCas) {
		boolean result = false;
		for (LocationCas lo : getAllLocations()) {
			if (lo.equals(locationCas)) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public LocationCas findByCountry(String country) {
		for (LocationCas lo : getAllLocations()) {
			if (lo.getCountry().equals(country)) {
				return lo;
			}
		}
		return null;
	}

	@Override
	public boolean deleteLocationByCountry(String country) {
		
		return false;
	}

}
