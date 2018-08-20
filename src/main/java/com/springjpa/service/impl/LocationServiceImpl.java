package com.springjpa.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.model.jpa.QLocation;
import com.springjpa.repository.LocationCasRepository;
import com.springjpa.repository.LocationRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.LocationService;
import com.springjpa.util.DataTimeUtil;

@Service
public class LocationServiceImpl extends BaseService implements LocationService {

	@PersistenceContext
	private EntityManager em;

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
		if (getAllLocations().isEmpty() || getAllLocations().size() == 0) {
			return false;
		}
		for (LocationCas lo : getAllLocations()) {
			System.out.println(lo.toString());
			if ((lo.getCountry().equals(locationCas.getCountry())) && (lo.getCity().equals(locationCas.getCity()))) {
				return true;
			}
		}
		return false;
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

	// Delete in Cas
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
		if (getAllLocationInJPA().isEmpty() || getAllLocationInJPA().size() == 0) {
			return false;
		}
		for (Location lo : getAllLocationInJPA()) {
			if ((lo.getCountry().equals(location.getCountry())) && (lo.getCity().equals(location.getCity()))) {
				return true;
			}
		}
		return false;
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
		locationRepository.deleteAll();
	}

	@Override
	public List<Location> getListLocationByQueryDslFromJpa(Predicate predicate) {

		List<Location> list = new ArrayList<>();
		jpaRepository.findAll(predicate).forEach(list::add);
		return list;
	}

	@Override
	public Location getOneLocationByQueryDslFromJpa(Predicate predicate) {
		Optional<Location> result = jpaRepository.findOne(predicate);
		if (!result.isPresent()) {
			throw new NoDataFoundException("Not found data in DB");
		}
		return result.get();
	}

	@Override
	public List<Location> getListLocationByQueryDslFromJpa(Predicate predicate, OrderSpecifier<?>... orders) {

		List<Location> list = new ArrayList<>();
		jpaRepository.findAll(predicate, orders).forEach(list::add);
		return list;
	}

	@Override
	public List<Location> getListLocationByQueryDslFromJpa(OrderSpecifier<?>... orders) {
		List<Location> list = new ArrayList<>();
		jpaRepository.findAll(orders).forEach(list::add);
		return list;
	}

	// Ordering
	@Override
	public List<Location> getListLocationSortByCountry() {
		JPAQuery<Location> query = new JPAQuery<Location>(em);
		QLocation qLocation = QLocation.location;
		List<Location> list = query.from(qLocation).orderBy(qLocation.country.asc(), qLocation.city.desc()).fetch();
		return list;
	}


	@Override
	public void deleteLocationJPA(UUID id) {
		JPAQueryFactory query = new JPAQueryFactory(em);
		QLocation qLocation = QLocation.location;
		query.delete(qLocation).where(qLocation.location_id.eq(id)).execute();
		
	}

	@Override
	public void updateLocationQueryDsl(Location location, String country, String city) {
		JPAQueryFactory query = new JPAQueryFactory(em);
		QLocation qLocation = QLocation.location;

		if (jpaRepository.exists(qLocation.country.eq(country))) {

			query.update(qLocation).where(qLocation.country.eq(location.getCountry())).set(qLocation.country, country)
					.execute();

			query.update(qLocation).where(qLocation.city.eq(location.getCity())).set(qLocation.city, city).execute();
		}
	}

	@Override
	public boolean isExistsLocationJPA(Predicate predicate) {
		if (jpaRepository.exists(predicate)) {
			return true;
		}
		return false;
	}

}
