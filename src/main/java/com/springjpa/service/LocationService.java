package com.springjpa.service;

import java.util.List;
import java.util.UUID;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;

public interface LocationService {

	// get
	public List<LocationCas> getAllLocations();

	public List<Location> getAllLocationInJPA();

	public LocationCas findByIdInCas(UUID id);

	public Location findByIdInJPA(UUID id);

	// add
	public LocationCas saveLocationCas(LocationCas locationCas);

	public Location saveLocationJPA(Location location);

	// update
	public LocationCas updateLocationInCas(LocationCas location);

	public Location updateLocationInJPA(Location location);

	// dalete
	public void deleteAllLocationInCas();

	public void deleteLocationById(UUID id);

	// check location
	public boolean isExistsLocation(LocationCas locationCas);

	public boolean isExistsLocationinJPA(Location location);

	/* use queryDsl */
	//get
	public Location getOneLocationByQueryDslFromJpa(Predicate predicate);

	public List<Location> getListLocationByQueryDslFromJpa(Predicate predicate);

	public List<Location> getListLocationByQueryDslFromJpa(Predicate predicate, OrderSpecifier<?>... orders);

	public List<Location> getListLocationByQueryDslFromJpa(OrderSpecifier<?>... orders);
	
	public boolean isExistsLocationJPA(Predicate predicate);
	
	// Ordering
	public List<Location> getListLocationSortByCountry();

	// delete
	public void deleteLocationJPA(UUID id);
	
	//UPDATE
	public void updateLocationQueryDsl(Location location,String country, String city);
	
}
