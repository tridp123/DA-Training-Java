package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.springjpa.model.cassandra.SalesCas;

public interface SalesCasRepository extends CrudRepository<SalesCas, UUID> {

}
