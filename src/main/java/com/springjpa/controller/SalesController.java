package com.springjpa.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springjpa.dto.SalesDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.SalesCas;
import com.springjpa.model.jpa.Sales;
import com.springjpa.model.jpa.SalesId;
import com.springjpa.service.SalesService;
import com.springjpa.service.impl.SalesServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RestController
@RequestMapping("/sales")
public class SalesController {

	public static final Logger log = LoggerFactory.getLogger(SalesController.class);

	@Autowired
	private SalesService salesService = new SalesServiceImpl();

	/* get */
	// -------------------Retrieve All Sales--------------------------------------------------------
	@GetMapping(value = "/getallsalescas", headers = "Accept=application/json")
	public ResponseEntity<List<SalesDTO>> getAllSalesCas() {
		List<SalesDTO> list = convertListSalesCas(salesService.getAllSalse());
		return new ResponseEntity<List<SalesDTO>>(list, HttpStatus.OK);
	}
	
	// add Sales info JPA
		@RequestMapping(value = "/addsales/addjpa", method = RequestMethod.POST)
		public ResponseEntity<SalesDTO> addSalesInJPA(@RequestBody SalesDTO sales) {

			SalesDTO result = convertToDTO(salesService.saveSalesJPA(convertToJPAEntity(sales)), DBType.JPA);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "http://localhost:8080/sales?product-id=" + sales.getProductId());
			return new ResponseEntity<SalesDTO>(result, headers, HttpStatus.CREATED);
		}
	
	

	public SalesDTO convertToDTO(Object obj, DBType type) {
		SalesDTO dto = null;
		if (obj == null) {
			throw new NoDataFoundException("Not found Sales");
		}
		if (type == DBType.JPA) {
			Sales sales = (Sales) obj;
			dto = new SalesDTO(sales.getId().getProductId(), sales.getId().getLocationId(), sales.getId().getTimeId(),
					sales.getDollars(), DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		} else if (type == DBType.CASSANDRA) {
			SalesCas sales = (SalesCas) obj;
			dto = new SalesDTO(sales.getProductId(), sales.getLocationId(), sales.getTimeId(), sales.getDollars(),
					DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		} else {
			throw new BadRequestException("No type");
		}
		return dto;
	}

	public List<SalesDTO> convertListSalesCas(Iterable<SalesCas> list) {
		List<SalesDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found Sales");
		}
		for (SalesCas lo : list) {
			SalesDTO dto = convertToDTO(lo, DBType.CASSANDRA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
	}

	public List<SalesDTO> convertListProductJPA(Iterable<Sales> list) {
		List<SalesDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found Sales");
		}
		for (Sales lo : list) {
			SalesDTO dto = convertToDTO(lo, DBType.JPA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
	}

	public Sales convertToJPAEntity(SalesDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		Sales sales = new Sales();
		sales.setId(new SalesId(dto.getProductId(), dto.getTimeId(), dto.getLocationId()));
		sales.setDollars(dto.getDollars());
		sales.setCreatedAt(new Timestamp(dto.getCreatedAt().getMillis()));
		sales.setModifiedAt(new Timestamp(dto.getCreatedAt().getMillis()));
		return sales;
	}

	public SalesCas convertToCassandraEntity(SalesDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		SalesCas sales = new SalesCas();
		sales.setProductId(dto.getProductId());
		sales.setTimeId(dto.getTimeId());
		sales.setLocationId(dto.getLocationId());
		sales.setDollars(dto.getDollars());
		sales.setCreatedAt(dto.getCreatedAt());
		sales.setModifiedAt(dto.getModifiedAt());
		return sales;
	}

}
