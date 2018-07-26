package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.springjpa.model.cassandra.TimeCas;

public interface TimeCasRepository extends CrudRepository<TimeCas, UUID>{

}
