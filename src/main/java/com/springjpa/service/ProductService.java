package com.springjpa.service;

import java.util.List;

import com.springjpa.model.cassandra.ProductCas;
import com.springjpa.model.jpa.Product;

public interface ProductService {

	// get
	public Iterable<ProductCas> getAllProduct();
	public Iterable<Product> getAllProductInJPA();
	public List<ProductCas> findByClassInCas(String sClass);
	public Product findByClassInJPA(String sClass);

	// add
	public ProductCas saveProductCas(ProductCas productCas);
	public Product saveProductJPA(Product product);

	// update
	public ProductCas updateProductInCas(ProductCas product,int item, String sClass, String inventory);
	public Product updateProductInJPA(Product Product, int item, String sClass, String inventory);

	// dalete
	public void deleteAllProductInCas();

	public void deleteProductByClass(String sClass);

	// check Product
	public boolean isExistsProductinCas(ProductCas productCas);
	public boolean isExistsProductinJPA(Product product);
}
