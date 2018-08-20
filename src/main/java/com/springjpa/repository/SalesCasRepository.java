package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.cassandra.SalesCas;
@Repository
public interface SalesCasRepository extends CrudRepository<SalesCas, UUID> {

}
