package com.springjpa.service;

import java.util.List;

import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;

public interface LocationService {

	//get
	public Iterable<LocationCas> getAllLocations();
	public Iterable<Location> getAllLocationInJPA();
	
	public List<LocationCas> findByCountryInCas(String country);
	public Location findByCountryInJPA(String country);

	//add
	public LocationCas saveLocationCas(LocationCas locationCas);
	public Location saveLocationJPA(Location location);

	//update
	public LocationCas updateLocationInCas(LocationCas location,String country, String city);
	public Location updateLocationInJPA(Location location,String country, String city);

	//dalete
	public void deleteAllLocationInCas();
	public void deleteLocationByCountry(String country);
	
	//check location
	public boolean isExistsLocation(LocationCas locationCas);
	public boolean isExistsLocationinJPA(Location location);
	
}
