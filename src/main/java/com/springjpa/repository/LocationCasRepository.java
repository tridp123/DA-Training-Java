package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.cassandra.LocationCas;

@Repository
public interface LocationCasRepository extends CrudRepository<LocationCas, UUID> {

}
