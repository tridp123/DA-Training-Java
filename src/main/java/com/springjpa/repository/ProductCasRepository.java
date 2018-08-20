package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.cassandra.ProductCas;
@Repository
public interface ProductCasRepository extends CrudRepository<ProductCas, UUID> {

	ProductCas getProductByItem(int item);

	ProductCas findOneByProductId(UUID id);

}
