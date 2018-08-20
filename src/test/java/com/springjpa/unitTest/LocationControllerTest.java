package com.springjpa.unitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springjpa.controller.BaseControllerExceptionHandler;
import com.springjpa.controller.LocationController;
import com.springjpa.dto.LocationDTO;
import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.service.LocationService;

//để có thể sử dụng annotation
@RunWith(MockitoJUnitRunner.class )
//tự động cấu hình cơ sở kiến trúc Spring MVC 
@WebMvcTest(controllers = LocationController.class)
public class LocationControllerTest {
	
/*Spring’s MockMvc
 * test HTTP request mà không cần start server
 * */
	public MockMvc mockMvc;

	// Inject under Mock info it (may be constructor, setter property and theo thu
	// tu)
	// đối tượng được test
	@InjectMocks
	LocationController locationController;

	//create mock object
	 /* mock(LocationController.class);
	 * or
	 * */
	@Mock
	LocationService locationServiceMock;

	UUID testUuid = UUID.fromString("14076a0e-3a18-4f3b-864e-f8ab52b23616");
	UUID testUuid2 = UUID.fromString("83b2004e-2096-4dc3-91ac-1bd2e06546ee");
	UUID wrongTestUuid = UUID.fromString("c381032b-7057-11e8-8754-c3e87a3ddddc");

	//set up trước mỗi method
	@Before
	public void setup() {
		System.out.println("Before method");
		mockMvc = MockMvcBuilders.standaloneSetup(locationController)
				.setControllerAdvice(new BaseControllerExceptionHandler()).build();
	}
	/*Test get*/
	
//	@Ignore
	@Test
	public void testGetAllLocation() throws Exception {
		LocationCas location1 = new LocationCas();
		location1.setLocationId(testUuid);
		LocationCas location2 = new LocationCas();
		location2.setLocationId(testUuid2);

		List<LocationCas> list = new ArrayList<LocationCas>();
		list.add(location1);
		list.add(location2);

		// danh sach tra ve cua pt getall giả thiết là list
		when(locationServiceMock.getAllLocations()).thenReturn(list);
		
		assertEquals(locationServiceMock.getAllLocations(), list);
		assertEquals(location1, list.get(0));

		mockMvc.perform(get("/location/getalllocationcas")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$[0].locationId", is(testUuid.toString())))
				.andExpect(jsonPath("$[1].locationId", is(testUuid2.toString()))).andDo(print());
		verify(locationServiceMock, atLeastOnce()).getAllLocations();
		verifyNoMoreInteractions(locationServiceMock);
	}
//	@Ignore
	@Test
	public void testGetLocationById() throws Exception {
		LocationCas location = new LocationCas();
		location.setLocationId(testUuid);
		when(locationServiceMock.findByIdInCas(testUuid)).thenReturn(location);
		mockMvc.perform(get("/location/getlocationcas/" + testUuid.toString())).andExpect(status().isOk())
				.andExpect(jsonPath("$.locationId", is(testUuid.toString()))).andDo(print());
	}
//	@Ignore
	@Test
	public void testGetlocationByNonexistentId() throws Exception {
		LocationCas location = new LocationCas();
		location.setLocationId(wrongTestUuid);
//		when(locationServiceMock.findByIdInCas(wrongTestUuid)).the

		mockMvc.perform(get("/location/getlocationcas/" + wrongTestUuid.toString())).andExpect(status().isNotFound());
	}
	/*Test add*/
//	@Ignore
	@Test
	public void testAddlocationCas() throws Exception {
		LocationCas cas = new LocationCas();
		cas.setLocationId(testUuid2);
		cas.setCountry("country2");
		cas.setCity("city2");
		when(locationServiceMock.saveLocationCas(Mockito.any(LocationCas.class))).thenReturn(cas);

		mockMvc.perform(
				post("/location/addlocation/addcas").param("country", cas.getCountry()).param("city", cas.getCity()))
				.andExpect(status().isCreated()).andDo(print());
		verify(locationServiceMock, times(1)).saveLocationCas(Mockito.any(LocationCas.class));
	}
//	@Ignore
	@Test(expected = NullPointerException.class)
	public void testAddnulllocation() throws Exception {
		Location l1 = null;
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(l1);
		mockMvc.perform(
				post("/location/addlocation/addjpa").param("country", l1.getCountry()).param("city", l1.getCity())
						.contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}
	
	/*Test Update*/
//	@Ignore
	@Test
	public void testUpdatelocation() throws Exception {

		LocationDTO dto = new LocationDTO();
		dto.setLocationId(testUuid);
		dto.setCountry("Mong Co update");
		dto.setCity("Bato update");
		
		LocationCas cas = new LocationCas();
		cas.setLocationId(testUuid);
		cas.setCountry("Mong Co update");
		cas.setCity("Bato update");
		

		when(locationServiceMock.findByIdInCas(testUuid)).thenReturn(cas);
		
		when(locationServiceMock.updateLocationInCas(Mockito.any(LocationCas.class))).thenReturn(cas);
		
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc.perform(put("/location/updateincas").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto)))
		.andExpect(status().isOk())
		.andDo(print());
		
		
		verify(locationServiceMock,times(1)).findByIdInCas(testUuid);
		verify(locationServiceMock,times(1)).updateLocationInCas(Mockito.any(LocationCas.class));
		
	}
	
	/*Test delete*/
//	@Ignore
	@Test
	public void testDeleteCas() throws Exception {
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				// danh sach cac đối số được sử dụng trong method
				Object[] args = invocation.getArguments();
				System.out.println("called with arguments: " + Arrays.toString(args));
				return null;
			}
		}).when(locationServiceMock).deleteLocationById(testUuid);
		locationServiceMock.deleteLocationById(testUuid);
		verify(locationServiceMock).deleteLocationById(testUuid);
		verifyNoMoreInteractions(locationServiceMock);
	}

}
