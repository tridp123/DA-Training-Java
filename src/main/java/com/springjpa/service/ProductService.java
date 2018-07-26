package com.springjpa.service;

import java.util.UUID;

import com.springjpa.model.cassandra.ProductCas;
import com.springjpa.model.jpa.Product;

public interface ProductService {

	public Iterable<ProductCas> getAllProducts();

	public ProductCas getProductByItem(int item);

	public ProductCas getProductById(UUID id);

//	public Product addProduct(Product product);

//	public Product updateProduct(Product product);

	public void deleteProductById(UUID id);
	
	public Iterable<Product> getAllProductsFromJpa();

	public Product getProductByIdFromJpa(UUID id);
	
//	public Product getOneProductByQueryDslFromJpa(Predicate predicate);
//	
//	public List<Product> getProductByQueryDslFromJpa(Predicate predicate);
}
