package com.springjpa.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springjpa.exception.NoDataFoundException;
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
	public boolean isExistsLocation(LocationCas locationCas) {
		boolean result = false;
		for (LocationCas lo : getAllLocations()) {
			if ((lo.getCountry().equals(locationCas.getCountry())) && (lo.getCity().equals(locationCas.getCity()))) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public List<LocationCas> findByCountryInCas(String country) {
		List<LocationCas> result = new ArrayList<>();
		for (LocationCas lo : getAllLocations()) {
			if (lo.getCountry().equals(country)) {
				result.add(lo);
			}
		}
		return result;
	}

	@Override
	public Location findByCountryInJPA(String country) {
		for (Location lo : getAllLocationInJPA()) {
			if (lo.getCountry().equals(country)) {
				return lo;
			}
		}
		return null;
	}

	
	//Delete in Cas
	@Override
	public void deleteLocationByCountry(String country) {
		for (LocationCas lo : getAllLocations()) {
			if (lo.getCountry().equals(country)) {
				locationRepository.delete(lo);
			}
		}
	}

	@Override
	public boolean isExistsLocationinJPA(Location location) {
		boolean result = false;
		for (Location lo : getAllLocationInJPA()) {
			if ((lo.getCountry().equals(location.getCountry())) && (lo.getCity().equals(location.getCity()))) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public Iterable<Location> getAllLocationInJPA() {
		return jpaRepository.findAll();
	}

	@Override
	public LocationCas updateLocationInCas(LocationCas location,String country, String city) {
		if ((locationRepository.findOne(location.getLocationId()) == null)) {
			throw new NoDataFoundException("Location ID '" + location.getLocationId() + "' not found in DB");
		}
		location.setCountry(country);
		location.setCity(city);
		location.setModifiedAt(DataTimeUtil.getCurrent());
		return locationRepository.save(location);
	}

	@Override
	public Location updateLocationInJPA(Location location, String country, String city) {
		if ((jpaRepository.findOne(location.getLocation_id()) == null)) {
			throw new NoDataFoundException("Location ID '" + location.getLocation_id() + "' not found in DB");
		}
		location.setCountry(country);
		location.setCity(city);
		location.setModifiedAt(new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		return jpaRepository.save(location);
	}

	@Override
	public void deleteAllLocationInCas() {
		 locationRepository.deleteAll();;
	}



}
