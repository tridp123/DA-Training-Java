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
import org.junit.Test;
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
/*hữu ích để tránh xung đột trong môi trường test*/
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationRepositoryTest {

//	@Rule
	// Trả về một quy tắc mà không có ngoại lệ được ném
	// When we need to verify some other properties of the exception, we can use the
	// ExpectedException rule
//	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	LocationRepository locationRepository;

	UUID testUUIDjpa = UUID.fromString("2f44e6c2-f2e2-4e1b-a98a-52dac603c5e6");
	
	UUID testUUID2 = UUID.fromString("22e7ae95-c645-4514-a978-99023a50de10");
	
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
	
//	@Ignore
	@Test
//	@Transactional
	public void testGetLocationById() {
		Optional<Location> result = locationRepository.findById(testUUIDjpa);
		System.out.println("testGetLocationById : " + result);
		assertEquals(testUUIDjpa, result.get().getLocation_id());
		assertTrue(result.isPresent());
	}
//	@Ignore
	// bỏ qua test này
	@Test
//	(timeout = 1000)
	@Transactional
	public void testGetLocationByNonExistentId() {
		Optional<Location> result = locationRepository.findById(wrongTestUuid);
		assertFalse(result.isPresent());
	}
	
	@Ignore
	@Test
//	@Transactional
	public void testGetAllLocations() {
		List<Location> result = new ArrayList<>();
		locationRepository.findAll().forEach(result::add);
		assertFalse(result.isEmpty());
		assertTrue(result.size() == 4);
		System.out.println("list all location: " + result);
	}
	
//	@Ignore
	@Test
//	@Transactional
	public void testAddLocation() {
		Location l = new Location(UUID.randomUUID(), "country", "city2",
				new Timestamp(DataTimeUtil.getCurrent().getMillis()),
				new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		Location result = locationRepository.save(l);

		System.out.println("Saved :" + result.toString());
		assertNotNull(result);
		assertEquals("country", result.getCountry());
		assertEquals("city2", result.getCity());
	}
	
//	@Ignore
	@Test(expected = InvalidDataAccessApiUsageException.class)
//	@Transactional
	public void testAddNullLocation() {
		locationRepository.save(null);
	}
	
//	@Ignore
	@Test
//	@Transactional
	public void testUpdateLocation() {
		Location result = locationRepository.save(
				new Location(testUUIDjpa, "ABCupdate", "CDFUPDATE", new Timestamp(DataTimeUtil.getCurrent().getMillis()),
						new Timestamp(DataTimeUtil.getCurrent().getMillis())));
		assertNotNull(result);
		assertEquals("ABCupdate", result.getCountry());
		assertEquals("CDFUPDATE", result.getCity());
	}
}
