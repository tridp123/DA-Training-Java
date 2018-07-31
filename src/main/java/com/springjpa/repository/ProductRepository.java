package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.jpa.Product;


@Repository
public interface ProductRepository extends CrudRepository<Product, UUID>{

}
