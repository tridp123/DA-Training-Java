package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.cassandra.TimeCas;
@Repository
public interface TimeCasRepository extends CrudRepository<TimeCas, UUID>{

}
