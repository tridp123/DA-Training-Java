package com.springjpa.unitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springjpa.controller.BaseControllerExceptionHandler;
import com.springjpa.controller.ProductController;
import com.springjpa.dto.ProductDTO;
import com.springjpa.model.jpa.Product;
import com.springjpa.service.ProductService;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {
	public MockMvc mockMvc;

	@InjectMocks
	private ProductController controller;

	@Mock
	private ProductService service;

	UUID testUuid = UUID.fromString("b3c38102-7057-11e8-8754-c3e87a3d914c");
	UUID testUuid2 = UUID.fromString("10b7f32a-fd4d-432b-8b53-d776db75b751");
	UUID wrongTestUuid = UUID.fromString("c381032b-7057-11e8-8754-c3e87a3ddddc");

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new BaseControllerExceptionHandler())
				.build();
	}

//	@Test
//	public void testUpdateProduct() throws Exception {
//		ProductDTO product = new ProductDTO();
//		product.setProductId(testUuid);
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(product);
//		when(service.updateProductInJPA(ArgumentMatchers.argThat(new ArgumentMatcher<Product>() {
//			@Override
//			public boolean matches(Product arg0) {
//				return arg0.getProductId().equals(testUuid);
//			}
//		}))).thenReturn(product);
//		mockMvc.perform(put("/product/update").contentType(MediaType.APPLICATION_JSON).content(json))
//				.andExpect(status().isOk()).andExpect(jsonPath("$.productId", is(testUuid.toString())));
//	}

}
