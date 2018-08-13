package com.springjpa.unitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springjpa.controller.BaseControllerExceptionHandler;
import com.springjpa.controller.LocationController;
import com.springjpa.dto.LocationDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.service.LocationService;
import com.springjpa.service.impl.LocationServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RunWith(MockitoJUnitRunner.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
//@SpringBootTest
public class LocationControllerTest {
	
	@Autowired
	public MockMvc mockMvc;

	//Inject under Mock info it (may be constructor, setter property and theo thu tu)
	@InjectMocks
	LocationController locationController;

	@Mock
	LocationService locationServiceMock;

	UUID testUuid = UUID.fromString("f65c6242-96d8-445e-a876-8bc4f7c4d83d");
	UUID testUuid2 = UUID.fromString("11ce0f39-d9e6-49d0-b2d5-3019555c02f4");
	UUID wrongTestUuid = UUID.fromString("c381032b-7057-11e8-8754-c3e87a3ddddc");
	
	@Before
	public void setup() {
		System.out.println("Before method");
		mockMvc = MockMvcBuilders.standaloneSetup(locationController)
				.setControllerAdvice(new BaseControllerExceptionHandler()).build();
	}
	@Ignore
	@Test
	public void test() throws Exception {
		LocationCas l1 = new LocationCas(UUID.fromString("f65c6242-96d8-445e-a876-8bc4f7c4d83d"), "USA", "New York",
				new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")),
				new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")));
		LocationCas l2 = new LocationCas(UUID.fromString("11ce0f39-d9e6-49d0-b2d5-3019555c02f4"), "Japan", "Tokyo",
				new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")),
				new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")));
//		LocationCas l3 = new LocationCas(UUID.randomUUID(), "Laos", "Vieng Chan", DataTimeUtil.getCurrent(),
//				DataTimeUtil.getCurrent());
		UUID u = UUID.fromString("3040f422-c1c5-4840-a534-dc245dbbb5ea");
//		List<LocationCas> list = new ArrayList<LocationCas>();
//		list.add(l1);
//		list.add(l2);
//		when(locationController.getLocationJPA("3040f422-c1c5-4840-a534-dc245dbbb5ea").getBody().getLocationId()).thenReturn(u);

//		assertEquals(locationController.getLocationJPA("3040f422-c1c5-4840-a534-dc245dbbb5ea").getBody().getLocationId(), u);

//		ResponseEntity<LocationDTO> a = locationController.getLocationJPA("3040f422-c1c5-4840-a534-dc245dbbb5ea");
//		System.out.println(a);

//
//		List<LocationCas> a = service.getAllLocations();
//		
//		System.out.println(a);
//		when(locationServiceMock.getAllLocations()).thenReturn(list);

//		List<LocationDTO> l = locationController.convertListLocationCas(list);

//		LocationDTO ldto1 = new LocationDTO(UUID.fromString("f65c6242-96d8-445e-a876-8bc4f7c4d83d"), "USA", "New York",
//				new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")), new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")));
//		LocationDTO ldto2 = new LocationDTO(UUID.fromString("11ce0f39-d9e6-49d0-b2d5-3019555c02f4"), "Japan", "Tokyo",
//				new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")), new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")));
//		List<LocationDTO> list2 = new ArrayList<>();
//		list2.add(ldto1);
//		list2.add(ldto2);
//		assertTrue(locationController.compareLocation(l1, locationServiceMock.findByIdInCas(UUID.fromString("f65c6242-96d8-445e-a876-8bc4f7c4d83d"))));
//		assertTrue(locationController.compareLocation(l1, locationServiceMock.findByIdInCas(UUID.fromString("f65c6242-96d8-445e-a876-8bc4f7c4d83d"))));

//		assertEquals(l1.getLocationId(), a.getLocationId());

//		LocationCas a = service.findByIdInCas(UUID.fromString("f65c6242-96d8-445e-a876-8bc4f7c4d83d"));
//		
//		System.out.println(a.toString());
//		
//		assertEquals(l1.getCountry(), a.getCountry());

//		mvc.perform(get("/location/getalllocationcas")).andExpect(status().isOk());
	}
	@Ignore
	@Test
	public void testGetAllLocation() throws Exception {
		LocationCas location1 = new LocationCas();
		location1.setLocationId(testUuid);
		LocationCas location2 = new LocationCas();
		location2.setLocationId(testUuid2);
		
		List<LocationCas> list = new ArrayList<LocationCas>();
		list.add(location1);
		list.add(location2);
		
//danh sach tra ve cua pt getall giả thiết là list
		when(locationServiceMock.getAllLocations()).thenReturn(list);
		assertEquals(locationServiceMock.getAllLocations(), list);
		assertEquals(location1, list.get(0));
		
		mockMvc.perform(get("/location/getalllocationcas")).andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2))).andExpect(jsonPath("$[0].locationId", is(testUuid.toString())))
		.andExpect(jsonPath("$[1].locationId", is(testUuid2.toString())));
		verify(locationServiceMock, atLeastOnce()).getAllLocations();
	}
	@Ignore
	@Test
	public void testGetLocationById() throws Exception {
		LocationCas location = new LocationCas();
		location.setLocationId(testUuid);
		when(locationServiceMock.findByIdInCas(testUuid)).thenReturn(location);
		mockMvc.perform(get("/location/getlocationcas/" + testUuid.toString())).andExpect(status().isOk())
				.andExpect(jsonPath("$.locationId", is(testUuid.toString())));
	}
	@Ignore
	@Test
	public void testGetlocationByNonexistentId() throws Exception {
		LocationCas location = new LocationCas();
		location.setLocationId(wrongTestUuid);
//		when(locationServiceMock.findByIdInCas(wrongTestUuid)).the

		mockMvc.perform(get("/location/getlocationcas/" + wrongTestUuid.toString())).andExpect(status().isNotFound());
	}
	
	@Test
	public void testAddlocation() throws Exception {
		Location l1 = new Location(testUuid, "USA", "New York", new Timestamp(DataTimeUtil.getCurrent().getMillis()), new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		Location l2 = new Location(testUuid, "USA", "New York", new Timestamp(DataTimeUtil.getCurrent().getMillis()), new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		Location l3 = new Location(testUuid2, "USA", "New York", new Timestamp(DataTimeUtil.getCurrent().getMillis()), new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		
		when(locationServiceMock.saveLocationJPA(l1)).thenReturn(l2);

		assertEquals(locationServiceMock.saveLocationJPA(l1), l2);
		System.out.println("vl"+locationServiceMock.saveLocationJPA(l1));
		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		mockMvc.perform(post("/location/addlocation/addjpa").param("country", l1.getCountry()).param("city", l1.getCity()).param(name, values)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.locationId", is(l1.getLocation_id().toString())));
	}

//	@Test
//	public void testAddNulllocation() throws Exception {
//		location location = null;
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(location);
//		mockMvc.perform(post("/location/add").contentType(MediaType.APPLICATION_JSON).content(json)
//				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//	}
//
//	@Test
//	public void testUpdatelocation() throws Exception {
//		location location = new location();
//		location.setlocationId(testUuid);
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(location);
//		when(service.updatelocation(ArgumentMatchers.argThat(new ArgumentMatcher<location>() {
//			@Override
//			public boolean matches(location arg0) {
//				return arg0.getlocationId().equals(testUuid);
//			}
//		}))).thenReturn(location);
//		mockMvc.perform(put("/location/update").contentType(MediaType.APPLICATION_JSON).content(json))
//				.andExpect(status().isOk()).andExpect(jsonPath("$.locationId", is(testUuid.toString())));
//	}
//
//	@Test
//	public void testUpdatelocationWithNonexistentId() throws Exception {
//		location location = new location();
//		location.setlocationId(wrongTestUuid);
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(location);
//		when(service.updatelocation(ArgumentMatchers.argThat(new ArgumentMatcher<location>() {
//			@Override
//			public boolean matches(location arg0) {
//				return arg0.getlocationId().equals(wrongTestUuid);
//			}
//		}))).thenReturn(null);
//		mockMvc.perform(put("/location/update").contentType(MediaType.APPLICATION_JSON).content(json))
//				.andExpect(status().isNotFound());
//	}

}
