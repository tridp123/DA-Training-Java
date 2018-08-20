package com.springjpa.integrationTest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springjpa.dto.LocationDTO;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LocationControllerTest {
//NO Mocking in Integration TEST

	UUID testUuidjpa = UUID.fromString("172f17ca-1881-424c-af4f-3272fcab49cc");

	UUID testUuidRD = UUID.randomUUID();

	UUID testUuidcas = UUID.fromString("ad366f93-9f5a-46e8-b224-2d6f5d60975f");

	UUID wrongTestUuid = UUID.fromString("52f3dc34-0ff5-43ee-9617-6037eecac897");

	public MockMvc mockMvc;

	@Resource
	private WebApplicationContext wac;

	private static final ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.alwaysDo(MockMvcResultHandlers.print()).build();
	}
	
	/*Test get*/
//	@Ignore
	@Test
	@Transactional
	public void testGetAllLocationsCas() throws Exception {
		mockMvc.perform(get("/location/getalllocationcas")).andExpect(status().isOk());
	}

//	@Ignore
	@Test
	@Transactional
	public void testGetAllLocationsJpa() throws Exception {
		mockMvc.perform(get("/location/getalllocationjpa")).andExpect(status().isOk());
	}
	//Test get Location Cassandra by id
//	@Ignore
	@Test
	@Transactional
	public void testGetLocationByIdCas() throws Exception {
		mockMvc.perform(get("/location/getlocationcas/" + testUuidcas)).andExpect(status().isOk());
	}

	//Test get Location JPA by id
//	@Ignore
	@Test
	@Transactional
	public void testGetLocationByIdJpa() throws Exception {
		mockMvc.perform(get("/location/getlocationjpa/" + testUuidjpa)).andExpect(status().isOk())
				.andExpect(jsonPath("$.locationId", is(testUuidjpa.toString()))).andDo(print());
	}

	// Test add location enter Location object
//	@Ignore
	@Test
	@Transactional
	public void testAddLocationCas() throws Exception {
		LocationDTO dto = new LocationDTO();
		dto.setLocationId(UUID.randomUUID());
		dto.setCountry("Mong Co 2");
		dto.setCity("Bato 2");
		mockMvc.perform(post("/location/addlocationcas").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(dto))).andExpect(status().isCreated()).andDo(print());
	}

	// Test add location enter 2 param (country,city)
//	@Ignore
	@Test
	public void testAddlocationCas2() throws Exception {
		mockMvc.perform(post("/location/addlocation/addcas").param("country", "country test").param("city", "city test"))
				.andExpect(status().isCreated()).andDo(print());
	}
	@Ignore
	// test add location is existed
	@Test
	public void testAddlocationCasisExisted() throws Exception {
		mockMvc.perform(post("/location/addlocation/addcas").param("country", "vcl2").param("city", "dkm2"))
				.andExpect(status().isConflict()).andDo(print());
	}

	
	/*test delete*/
	@Ignore
	@Test
	@Transactional
	public void testDeleteCasById() throws Exception {
		mockMvc.perform(delete("/location/delete/" + testUuidcas)).andExpect(status().isNoContent()).andDo(print());
	}

	@Ignore
	@Test
	@Transactional
	public void testDeleteAllCas() throws Exception {
		mockMvc.perform(delete("/location/deleteall")).andExpect(status().isNoContent()).andDo(print());
	}

}
