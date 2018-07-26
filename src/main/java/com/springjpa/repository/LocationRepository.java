package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.jpa.Location;


@Repository
public interface LocationRepository extends CrudRepository<Location, UUID> {

}
