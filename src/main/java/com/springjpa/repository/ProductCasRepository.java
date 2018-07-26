package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.springjpa.model.cassandra.ProductCas;

public interface ProductCasRepository extends CrudRepository<ProductCas, UUID> {

	ProductCas getProductByItem(int item);

	ProductCas findOneByProductId(UUID id);

}
