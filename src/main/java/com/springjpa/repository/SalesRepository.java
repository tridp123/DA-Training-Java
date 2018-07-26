package com.springjpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.jpa.Sales;
import com.springjpa.model.jpa.SalesId;


@Repository
public interface SalesRepository extends CrudRepository<Sales, SalesId>{

}
