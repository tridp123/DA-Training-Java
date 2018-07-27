package com.springjpa.service;

import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;

public interface LocationService {

	//get
	public Iterable<LocationCas> getAllLocations();
	public LocationCas findByCountry(String country);

	//add
	public LocationCas saveLocationCas(LocationCas locationCas);

	public Location saveLocationJPA(Location location);

	//update
	public Location updateLocation(Location location);

	//dalete
	public boolean deleteAllLocation();
	
	public boolean deleteLocationByCountry(String country);
	
	//check location
	public boolean isExistsLocation(LocationCas locationCas);
	
	
}
