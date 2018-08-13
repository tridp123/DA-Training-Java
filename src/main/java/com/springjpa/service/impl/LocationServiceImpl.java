package com.springjpa.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	public List<LocationCas> getAllLocations() {
		List<LocationCas> a = new ArrayList<>();
		locationRepository.findAll().forEach(a::add);
		return a;
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
			System.out.println("contry: "+lo.getCountry()+"/n"+"city: "+lo.getCity());
			if ((lo.getCountry().equals(locationCas.getCountry())) && (lo.getCity().equals(locationCas.getCity()))) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public LocationCas findByIdInCas(UUID id) {
		LocationCas result = null;
		for (LocationCas lo : getAllLocations()) {
			if (lo.getLocationId().equals(id)) {
				result = lo;
			}
		}
		return result;
	}

	@Override
	public Location findByIdInJPA(UUID id) {
		for (Location lo : getAllLocationInJPA()) {
			if (lo.getLocation_id().equals(id)) {
				return lo;
			}
		}
		return null;
	}
	
	//Delete in Cas
	@Override
	public void deleteLocationById(UUID id) {
		for (LocationCas lo : getAllLocations()) {
			if (lo.getLocationId().equals(id)) {
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
	public List<Location> getAllLocationInJPA() {
		List<Location> a = new ArrayList<>();
		jpaRepository.findAll().forEach(a::add);
		return a;
	}

	@Override
	public LocationCas updateLocationInCas(LocationCas location) {
		if ((locationRepository.findById(location.getLocationId()) == null)) {
			throw new NoDataFoundException("Location ID '" + location.getLocationId() + "' not found in DB");
		}
		location.setModifiedAt(DataTimeUtil.getCurrent());
		return locationRepository.save(location);
	}

	@Override
	public Location updateLocationInJPA(Location location) {
		if ((jpaRepository.findById(location.getLocation_id()) == null)) {
			throw new NoDataFoundException("Location ID '" + location.getLocation_id() + "' not found in DB");
		}
		location.setModifiedAt(new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		return jpaRepository.save(location);
	}

	@Override
	public void deleteAllLocationInCas() {
		 locationRepository.deleteAll();;
	}
}
