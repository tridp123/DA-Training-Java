package com.springjpa.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.cassandra.ProductCas;
import com.springjpa.model.jpa.Product;
import com.springjpa.repository.ProductCasRepository;
import com.springjpa.repository.ProductRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.ProductService;
import com.springjpa.util.DataTimeUtil;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

	@Autowired
	private ProductCasRepository cassRepository;

	@Autowired
	private ProductRepository jpaRepository;

	public ProductServiceImpl() {
	}

	@Override
	public Iterable<ProductCas> getAllProduct() {
		return cassRepository.findAll();
	}

	@Override
	public Iterable<Product> getAllProductInJPA() {
		return jpaRepository.findAll();
	}

	@Override
	public List<ProductCas> findByClassInCas(String sClass) {
		List<ProductCas> result = new ArrayList<>();
		for (ProductCas lo : getAllProduct()) {
			if (lo.getsClass().equals(sClass)) {
				result.add(lo);
			}
		}
		return result;
	}

	@Override
	public Product findByClassInJPA(String sClass) {
		for (Product lo : getAllProductInJPA()) {
			if (lo.getsClass().equals(sClass)) {
				return lo;
			}
		}
		return null;
	}

	@Override
	public ProductCas saveProductCas(ProductCas productCas) {
		return cassRepository.save(productCas);
	}

	@Override
	public Product saveProductJPA(Product product) {
		return jpaRepository.save(product);
	}

	@Override
	public ProductCas updateProductInCas(ProductCas product, int item, String sClass, String inventory) {
		if ((cassRepository.findById(product.getProductId()) == null)) {
			throw new NoDataFoundException("Product ID '" + product.getProductId() + "' not found in DB");
		}
		product.setItem(item);
		product.setsClass(sClass);
		product.setInventory(inventory);
		product.setModifiedAt(DataTimeUtil.getCurrent());
		return cassRepository.save(product);
	}

	@Override
	public Product updateProductInJPA(Product product, int item, String sClass, String inventory) {
		if ((jpaRepository.findById(product.getProductId()) == null)) {
			throw new NoDataFoundException("Product ID '" + product.getProductId() + "' not found in DB");
		}
		product.setItem(item);
		product.setsClass(sClass);
		product.setInventory(inventory);
		product.setModifiedAt(new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		return jpaRepository.save(product);
	}

	@Override
	public void deleteAllProductInCas() {
		cassRepository.deleteAll();
	}

	@Override
	public void deleteProductByClass(String sClass) {
		for (ProductCas pro : getAllProduct()) {
			if (pro.getsClass().equals(sClass)) {
				cassRepository.delete(pro);
			}
		}
	}

	@Override
	public boolean isExistsProductinCas(ProductCas productCas) {
		boolean result = false;
		for (ProductCas lo : getAllProduct()) {
			if (lo.getItem()==productCas.getItem() && (lo.getsClass().equals(productCas.getsClass()))
					&& (lo.getInventory().equals(productCas.getInventory()))) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean isExistsProductinJPA(Product product) {
		boolean result = false;
		for (Product pro : getAllProductInJPA()) {
			if (pro.getItem() == product.getItem() && (pro.getsClass().equals(product.getsClass()))
					&& (pro.getInventory().equals(product.getInventory()))) {
				result = true;
				break;
			}
		}
		return result;
	}

}
