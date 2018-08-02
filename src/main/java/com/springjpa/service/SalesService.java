package com.springjpa.service;

import java.util.List;

import com.springjpa.model.cassandra.SalesCas;
import com.springjpa.model.jpa.Sales;

public interface SalesService {
	
	public List<SalesCas> getAllSalse();
	
	public Sales saveSalesJPA(Sales sales);
	
	public SalesCas saveSalesCas(SalesCas sales);
	
	public Sales updateSalesInJPA(Sales sales,int dollars);
	
}
