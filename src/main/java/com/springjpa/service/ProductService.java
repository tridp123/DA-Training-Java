package com.springjpa.service;

import java.util.List;
import java.util.UUID;

import com.springjpa.model.cassandra.ProductCas;
import com.springjpa.model.jpa.Product;

public interface ProductService {

	// get
	public List<ProductCas> getAllProduct();
	public List<Product> getAllProductInJPA();
	public ProductCas findByIdInCas(UUID id);
	public Product findByIdInJPA(UUID id);

	// add
	public ProductCas saveProductCas(ProductCas productCas);
	public Product saveProductJPA(Product product);

	// update
	public ProductCas updateProductInCas(ProductCas product,int item, String sClass, String inventory);
	public Product updateProductInJPA(Product Product);

	// dalete
	public void deleteAllProductInCas();

	public void deleteProductById(UUID id);

	// check Product
	public boolean isExistsProductinCas(ProductCas productCas);
	public boolean isExistsProductinJPA(Product product);
}
