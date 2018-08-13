package com.springjpa.unitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.repository.LocationCasRepository;
import com.springjpa.repository.LocationRepository;
import com.springjpa.service.impl.LocationServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {
	
	@InjectMocks
	private LocationServiceImpl service;

	@Mock
	private LocationCasRepository casrepository;

	@Mock
	private LocationRepository jpaRepository;

	UUID testUuid = UUID.fromString("f65c6242-96d8-445e-a876-8bc4f7c4d83d");//new york
	UUID testUuid2 = UUID.fromString("11ce0f39-d9e6-49d0-b2d5-3019555c02f4");//tokyo
	UUID wrongTestUuid = UUID.fromString("c381032b-7057-11e8-8754-c3e87a3ddddc");

	@Test
	public void testGetAllLocations() {
		LocationCas l1 = new LocationCas(testUuid, "USA", "New York", new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")),new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")));
		LocationCas l2 = new LocationCas(testUuid2, "Japan", "Tokyo", new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")),new DateTime(2018, 8, 8, 11, 48, 18, DateTimeZone.forID("UTC")));
	
		List<LocationCas> list = new ArrayList<LocationCas>();
		list.add(l1);
		list.add(l2);
		
		when(casrepository.findAll()).thenReturn(list);
//		when(service.getAllLocations()).thenReturn(list);
		
		assertEquals(casrepository.findAll(),list);
		assertTrue(service.isExistsLocation(l2));
	}
}
