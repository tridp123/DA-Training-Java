package com.springjpa.unitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.springjpa.model.jpa.Location;
import com.springjpa.repository.LocationRepository;
import com.springjpa.util.DataTimeUtil;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class LocationRepositoryTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	LocationRepository locationRepository;

	UUID testUuid = UUID.fromString("2488cfb7-3aac-4d0c-b825-81516f967093");
	UUID testUuid2 = UUID.fromString("ab6509f3-5d79-4daa-aadd-9544541f154a");
	UUID wrongTestUuid = UUID.fromString("c381032b-7057-11e8-8754-c3e87a3ddddc");

	@BeforeClass
	public static void runOnceBeforeClass() {
		System.out.println("@BeforeClass - runOnceBeforeClass");
	}

	// Run once, e.g close connection, cleanup
	@AfterClass
	public static void runOnceAfterClass() {
		System.out.println("@AfterClass - runOnceAfterClass");
	}

	// Should rename to @BeforeTestMethod
	// e.g. Creating an similar object and share for all @Test
	@Before
	public void runBeforeTestMethod() {
		System.out.println("@Before - runBeforeTestMethod");
	}

	// Should rename to @AfterTestMethod
	@After
	public void runAfterTestMethod() {
		System.out.println("@After - runAfterTestMethod");
	}

	@Test
	@Transactional
	public void testGetLocationById() {
		Optional<Location> result = locationRepository.findById(testUuid2);
		assertEquals(testUuid2, result.get().getLocation_id());
		assertTrue(result.isPresent());
	}

	@Ignore // bỏ qua test này
	@Test
//	(timeout = 1000)
	@Transactional
	public void testGetLocationByNonExistentId() {
		Optional<Location> result = locationRepository.findById(wrongTestUuid);
		assertFalse(result.isPresent());
		thrown.expect(NullPointerException.class);
	}

	@Test
	@Transactional
	public void testGetAllLocations() {
		List<Location> result = new ArrayList<>();
		locationRepository.findAll().forEach(result::add);
		assertFalse(result.isEmpty());
		assertTrue(result.size() == 7);
		System.out.println("list all location: " + result);
	}

	@Test
	@Transactional
	public void testAddLocation() {
		Location result = locationRepository.save(
				new Location(UUID.randomUUID(), "ABC", "CDF", new Timestamp(DataTimeUtil.getCurrent().getMillis()),
						new Timestamp(DataTimeUtil.getCurrent().getMillis())));
		System.out.println("Saved :" + result.toString());
		assertNotNull(result);
		assertEquals("ABC", result.getCountry());
		assertEquals("CDF", result.getCity());
	}

	@Test(expected = InvalidDataAccessApiUsageException.class)
	@Transactional
	public void testAddNullLocation() {
		locationRepository.save(null);
	}

	@Test
	@Transactional
	public void testUpdateLocation() {
		Location result = locationRepository.save(new Location(UUID.randomUUID(), "ABCupdate", "CDFUPDATE",
				new Timestamp(DataTimeUtil.getCurrent().getMillis()),
				new Timestamp(DataTimeUtil.getCurrent().getMillis())));
		assertNotNull(result);
		assertEquals("ABCupdate", result.getCountry());
		assertEquals("CDFUPDATE", result.getCity());
	}
}
