package com.springjpa.service;


import com.springjpa.dto.LocationDTO;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;


public interface LocationService {

	public Iterable<LocationCas> getAllLocations();

	public LocationCas saveLocationCas(LocationCas locationCas);
	
	
	public Location saveLocationJPA(Location location);
//
//	public Location updateLocation(Location location);
//
//	public Location getOneLocationByQueryDslFromJpa(Predicate predicate);
//
//	public List<Location> getLocationByQueryDslFromJpa(Predicate predicate);
	
	public LocationDTO convertToDTO(Object obj, DBType type);
	public Location convertToJPAEntity(LocationDTO dto);
	public LocationCas convertToCassandraEntity(LocationDTO dto) ;
}
