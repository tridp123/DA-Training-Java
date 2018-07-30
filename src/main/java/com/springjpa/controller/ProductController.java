package com.springjpa.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springjpa.dto.ProductDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.ProductCas;
import com.springjpa.model.jpa.Product;
import com.springjpa.service.ProductService;
import com.springjpa.service.impl.ProductServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RestController
@RequestMapping("/product")
public class ProductController {

	public static final Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService productService = new ProductServiceImpl();
	
	/* get */
	// -------------------Retrieve All Product--------------------------------------------------------
	@GetMapping(value = "/getallproductcas", headers = "Accept=application/json")
	public ResponseEntity<List<ProductDTO>> getAllProductCas() {
		List<ProductDTO> list = convertListProductCas(productService.getAllProduct());
		return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/getallproductjpa", headers = "Accept=application/json")
	public ResponseEntity<List<ProductDTO>> getAllProductJPA() {
		List<ProductDTO> list = convertListProductJPA(productService.getAllProductInJPA());
		return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);
	}

	// -------------------Retrieve Single Product by Class--------------------------------------------------------
	@RequestMapping(value = "/getproductcas/{sClass}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> getProductCas(@PathVariable("sClass") String sClass) {
		
		List<ProductCas> ProductCas = productService.findByClassInCas(sClass);
		
		List<ProductDTO> list = new ArrayList<>();
		
		for (ProductCas cas : ProductCas) {
			list.add(convertToDTO(cas, DBType.CASSANDRA));
		}
		if (list == null) {
			return new ResponseEntity<List<ProductDTO>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/getproductjpa/{sClass}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDTO> getProductJPA(@PathVariable("sClass") String sClass) {
		System.out.println("Fetching Product with sClass " + sClass);
		ProductDTO ProductDTO = convertToDTO(productService.findByClassInJPA(sClass), DBType.JPA);
		if (ProductDTO == null) {
			System.out.println("Product with id " + ProductDTO.getProductId() + " not found");
			return new ResponseEntity<ProductDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("Product DTO Class: " + ProductDTO.getsClass());
		return new ResponseEntity<ProductDTO>(ProductDTO, HttpStatus.OK);
	}
	
	// add product info Cas
		@RequestMapping(value = "/addproduct/addcas", method = RequestMethod.POST)
		public ResponseEntity<ProductDTO> createProduct(@RequestParam int item, @RequestParam String sClass,@RequestParam String inventory,
				UriComponentsBuilder ucBuilder) {
			ProductCas pro = new ProductCas(UUID.randomUUID(), item,sClass, inventory, DataTimeUtil.getCurrent(),
					DataTimeUtil.getCurrent());

			if (productService.isExistsProductinCas(pro)) {
				System.out.println("A product with class" + pro.getsClass() + " already exist");
				return new ResponseEntity<ProductDTO>(HttpStatus.CONFLICT);
			}
			ProductCas a = productService.saveProductCas(pro);

			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(
					ucBuilder.path("/product/getproductCas/{sClass}").buildAndExpand(a.getClass()).toUri());
			return new ResponseEntity<ProductDTO>(convertToDTO(a, DBType.CASSANDRA), headers, HttpStatus.CREATED);
		}

		// add product info JPA
		@RequestMapping(value = "/addproduct/addjpa", method = RequestMethod.POST)
		public ResponseEntity<ProductDTO> addProductInJPA(@RequestParam int item, @RequestParam String sClass,@RequestParam String inventory,
				UriComponentsBuilder ucBuilder) {
			Product pro = new Product(UUID.randomUUID(), item,sClass, inventory,new Timestamp(DataTimeUtil.getCurrent().getMillis()) ,
					new Timestamp(DataTimeUtil.getCurrent().getMillis()));

			if (productService.isExistsProductinJPA(pro)) {
				System.out.println("A product with class" + pro.getsClass() + " already exist");
				return new ResponseEntity<ProductDTO>(HttpStatus.CONFLICT);
			}
			Product a = productService.saveProductJPA(pro);

			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(
					ucBuilder.path("/product/getproductCas/{sClass}").buildAndExpand(a.getClass()).toUri());
			return new ResponseEntity<ProductDTO>(convertToDTO(a, DBType.CASSANDRA), headers, HttpStatus.CREATED);
		}
		
		//add all product from CAS into JPA
		@RequestMapping(value = "/addallproduct", method = RequestMethod.POST)
		public ResponseEntity<Iterable<Product>> saveAllCasIntoJPA(){
			for (ProductDTO pro : convertListProductCas(productService.getAllProduct())) {
				productService.saveProductJPA(convertToJPAEntity(pro));
			}
			return new ResponseEntity<Iterable<Product>>(productService.getAllProductInJPA(), HttpStatus.CREATED);
		}
		
		// update JPA
		@PutMapping(value = "/updateinjpa", headers = "Accept=application/json")
		public ResponseEntity<ProductDTO> updateLocation( @RequestParam int item,
				@RequestParam String sClass,@RequestParam String inventory, UriComponentsBuilder ucBuilder) {
			Product pro = productService.findByClassInJPA(sClass);
			productService.updateProductInJPA(pro, item, sClass, inventory);

			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(
					ucBuilder.path("/product/getproductjpa/{sClass}").buildAndExpand(pro.getsClass()).toUri());
			return new ResponseEntity<ProductDTO>(convertToDTO(pro, DBType.JPA), headers, HttpStatus.OK);
		}
		// delete all product in Cas
		@DeleteMapping(value = "deleteall", headers = "Accept=application/json")
		public ResponseEntity<ProductDTO> deleteAllLocation() {
			productService.deleteAllProductInCas();
			return new ResponseEntity<ProductDTO>(HttpStatus.NO_CONTENT);
		}

		//
		@RequestMapping(value = "/delete/{sClass}", method = RequestMethod.DELETE)
		public ResponseEntity<List<ProductDTO>> deleteProduct(@PathVariable("sClass") String sClass) {

			List<ProductCas> dto = productService.findByClassInCas(sClass);
			if (dto == null) {
				return new ResponseEntity<List<ProductDTO>>(HttpStatus.NOT_FOUND);
			}
			
			productService.deleteProductByClass(sClass);
			return new ResponseEntity<List<ProductDTO>>(HttpStatus.NO_CONTENT);
		}
	
	public ProductDTO convertToDTO(Object obj, DBType type) {
		ProductDTO dto = null;
		if (obj == null) {
			throw new NoDataFoundException("Not found Product");
		}
		if (type == DBType.JPA) {
			Product product = (Product) obj;
			dto = new ProductDTO(product.getProductId(), product.getItem(), product.getsClass(),product.getInventory(),
					new DateTime(product.getCreatedAt()), new DateTime(product.getModifiedAt()));
		} else if (type == DBType.CASSANDRA) {
			ProductCas product = (ProductCas) obj;
			dto = new ProductDTO(product.getProductId(), product.getItem(), product.getsClass(),product.getInventory(),
					product.getCreatedAt(), product.getModifiedAt());
		} else {
			throw new BadRequestException("No type");
		}
		return dto;
	}

	public List<ProductDTO> convertListProductCas(Iterable<ProductCas> list) {
		List<ProductDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found Product");
		}
		for (ProductCas lo : list) {
			ProductDTO dto = convertToDTO(lo, DBType.CASSANDRA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
	}

	public List<ProductDTO> convertListProductJPA(Iterable<Product> list) {
		List<ProductDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found Product");
		}
		for (Product lo : list) {
			ProductDTO dto = convertToDTO(lo, DBType.JPA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
	}

	public Product convertToJPAEntity(ProductDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		Product Product = new Product(dto.getProductId(), dto.getItem(), dto.getsClass(),dto.getInventory(),
				new Timestamp(dto.getCreatedAt().getMillis()), new Timestamp(dto.getModifiedAt().getMillis()));
		return Product;
	}

	public ProductCas convertToCassandraEntity(ProductDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		ProductCas Product = new ProductCas(dto.getProductId(), dto.getItem(), dto.getsClass(),dto.getInventory(), dto.getCreatedAt(),
				dto.getModifiedAt());
		return Product;
	}
}
