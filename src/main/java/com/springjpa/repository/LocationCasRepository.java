package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.springjpa.model.cassandra.LocationCas;

public interface LocationCasRepository extends CrudRepository<LocationCas, UUID> {

}
