package com.springjpa.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.cassandra.ProductCas;
import com.springjpa.model.jpa.Product;
import com.springjpa.repository.ProductCasRepository;
import com.springjpa.repository.ProductRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.ProductService;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

	@Autowired
	private ProductCasRepository cassRepository;

	@Autowired
	private ProductRepository jpaRepository;

	@Override
	@Transactional(readOnly = true)
	public Iterable<ProductCas> getAllProducts() {
		return cassRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductCas getProductByItem(int item) {
		return cassRepository.getProductByItem( item);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductCas getProductById(UUID id) {
		ProductCas product = cassRepository.findOneByProductId(id);
		if (product == null) {
			throw new NoDataFoundException("Product ID '" + id + "' not found in DB");
		}
		return product;
	}

//	@Override
//	@PreAuthorize("hasRole('ADMIN')")
//	@Transactional(readOnly = false)
//	public Product addProduct(Product product) {
//		product.setCreatedAt(DateTimeUtil.getCurrent());
//		product.setModifiedAt(DateTimeUtil.getCurrent());
//		return jpaRepository.save(product);
//	}

//	@Override
//	@PreAuthorize("hasRole('ADMIN')")
//	@Transactional(readOnly = false)
//	public Product updateProduct(Product product) {
//		if (!jpaRepository.findById(product.getProductId()).isPresent()) {
//			throw new NoDataFoundException("Product ID '" + product.getProductId() + "' not found in DB");
//		}
//		product.setModifiedAt(DateTimeUtil.getCurrent());
//		// int row = jpaRepository.setProductByProductId(product.getItem(),
//		// product.getsClass(),
//		// product.getInventory(), product.getModifiedAt(),
//		// product.getProductId());
//		return jpaRepository.save(product);
//	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Product> getAllProductsFromJpa() {
		return jpaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Product getProductByIdFromJpa(UUID id) {
		Product product = jpaRepository.findOne(id);
		if (product==null) {
			throw new NoDataFoundException("Product ID '" + id + "' not found in DB");
		}
		return product;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteProductById(UUID id) {
		jpaRepository.delete(id);
	}

//	@Override
//	@Transactional(readOnly = true)
//	public List<Product> getProductByQueryDslFromJpa(Predicate predicate) {
//		List<Product> list = new ArrayList<>();
//		jpaRepository.findAll(predicate).forEach(list::add);
//		return list;
//	}
//
//	@Override
//	@Transactional(readOnly = true)
//	public Product getOneProductByQueryDslFromJpa(Predicate predicate) {
//		Product result = jpaRepository.findOne(predicate);
//		if (result==null) {
//			throw new NoDataFoundException("Not found data in DB");
//		}
//		return result;
//	}
}
