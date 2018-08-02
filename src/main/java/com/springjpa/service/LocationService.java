package com.springjpa.service;

import java.util.List;
import java.util.UUID;

import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;

public interface LocationService {

	//get
	public List<LocationCas> getAllLocations();
	public List<Location> getAllLocationInJPA();
	
	public LocationCas findByIdInCas(UUID id);
	public Location findByIdInJPA(UUID id);

	//add
	public LocationCas saveLocationCas(LocationCas locationCas);
	public Location saveLocationJPA(Location location);

	//update
	public LocationCas updateLocationInCas(LocationCas location,String country, String city);
	public Location updateLocationInJPA(Location location,String country, String city);

	//dalete
	public void deleteAllLocationInCas();
	public void deleteLocationById(UUID id);
	
	//check location
	public boolean isExistsLocation(LocationCas locationCas);
	public boolean isExistsLocationinJPA(Location location);
	
}
