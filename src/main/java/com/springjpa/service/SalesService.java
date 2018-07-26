package com.springjpa.service;

import com.springjpa.model.cassandra.SalesCas;

public interface SalesService {

	public Iterable<SalesCas> getAllSales();

//	public Sales addSale(Sales sales);
//
//	public Sales updateSale(Sales sales);

//	public List<Sales> getSalesByQueryDslFromJpa(Predicate predicate);
}
