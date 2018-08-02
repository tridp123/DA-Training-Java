package com.springjpa.service.impl;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.cassandra.SalesCas;
import com.springjpa.model.jpa.Sales;
import com.springjpa.model.jpa.SalesId;
import com.springjpa.repository.SalesCasRepository;
import com.springjpa.repository.SalesRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.SalesService;
import com.springjpa.util.DataTimeUtil;

@Service
public class SalesServiceImpl extends BaseService implements SalesService {

	@Autowired
	public SalesCasRepository cassRepository;

	@Autowired
	public SalesRepository jpaRepository;

	@Override
	public List<SalesCas> getAllSalse() {
		List<SalesCas> a = new ArrayList<SalesCas>();
		cassRepository.findAll().forEach(a::add);
		return a;
	}

	@Override
	public Sales saveSalesJPA(Sales sales) {
		return jpaRepository.save(sales);
	}

	@Override
	public Sales updateSalesInJPA(Sales sales, int dollars) {
		if (jpaRepository.findById(sales.getId())==null) {
			throw new NoDataFoundException("Sales not found");
		}
		sales.setDollars(dollars);
		sales.setModifiedAt(new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		return jpaRepository.save(sales);
	}

	@Override
	public SalesCas saveSalesCas(SalesCas sales) {
		return cassRepository.save(sales);
	}
}
