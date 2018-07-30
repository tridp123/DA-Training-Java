package com.springjpa.service;

import com.springjpa.model.cassandra.SalesCas;
import com.springjpa.model.jpa.Product;
import com.springjpa.model.jpa.Sales;

public interface SalesService {
	
	public Iterable<SalesCas> getAllSalse();
	
	public Sales saveSalesJPA(Sales sales);
	
	public Sales updateSalesInJPA(Sales sales,int dollars);
	
}
