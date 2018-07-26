package com.springjpa.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springjpa.model.cassandra.SalesCas;
import com.springjpa.repository.SalesCasRepository;
import com.springjpa.repository.SalesRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.SalesService;

@Service
public class SalesServiceImpl extends BaseService implements SalesService {

	@Autowired
	public SalesCasRepository cassRepository;

	@Autowired
	public SalesRepository jpaRepository;

	@Override
	@Transactional(readOnly = true)
	public Iterable<SalesCas> getAllSales() {
		return cassRepository.findAll();
	}

//	@Override
//	@PreAuthorize("hasRole('ADMIN')")
//	@Transactional(readOnly = false)
//	public Sales addSale(Sales sales) {
//		sales.setCreatedAt(DateTimeUtil.getCurrent());
//		sales.setModifiedAt(DateTimeUtil.getCurrent());
//		return jpaRepository.save(sales);
//	}
//
//	@Override
//	@PreAuthorize("hasRole('ADMIN')")
//	@Transactional(readOnly = false)
//	public Sales updateSale(Sales sales) {
//		if (!jpaRepository.findById(sales.getId()).isPresent()) {
//			throw new NoDataFoundException("Sales not found");
//		}
//		sales.setModifiedAt(DateTimeUtil.getCurrent());
//		return jpaRepository.save(sales);
//	}

//	@Override
//	@Transactional(readOnly = true)
//	public List<Sales> getSalesByQueryDslFromJpa(Predicate predicate) {
//		List<Sales> list = new ArrayList<>();
//		jpaRepository.findAll(predicate).forEach(list::add);
//		return list;
//	}
}
