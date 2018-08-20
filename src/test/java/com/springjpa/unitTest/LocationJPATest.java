package com.springjpa.unitTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.springjpa.model.jpa.Location;

public class LocationJPATest {

	UUID testUuid = UUID.fromString("b3c38102-7057-11e8-8754-c3e87a3d914c");
	UUID testUuid2 = UUID.fromString("10b7f32a-fd4d-432b-8b53-d776db75b751");
	UUID wrongTestUuid = UUID.fromString("c381032b-7057-11e8-8754-c3e87a3ddddc");

	@Test
	public void testGetLocationID() {
		// tạo mock
		Location location = mock(Location.class);
		location.setLocation_id(testUuid);

		//định nghĩa giá trị trả về của method getlocation_id
		//optionally, you can stub out some methods
		when(location.getLocation_id()).thenReturn(testUuid);

		// sử dụng mock in test
		assertEquals(location.getLocation_id(), testUuid);
	}

	@Test
	public void testGetLocationID2() {
		// tạo mock
		Location location = new Location();
		location.setLocation_id(testUuid);
		Location spy = spy(location);

		// định nghĩa giá trị trả về của method getlocation_id
		when(spy.getLocation_id()).thenReturn(testUuid);

		// sử dụng mock in test
		assertEquals(location.getLocation_id(), testUuid);
	}

	@Test
	public void testMoreThanOneReturnValue() {
		Iterator<String> i = mock(Iterator.class);
		when(i.next()).thenReturn("Mockito").thenReturn("rocks");
		String result = i.next() + " " + i.next() + " ad";
		// assert
		assertEquals("Mockito rocks ad", result);
	}

	// this test demonstrates how to return values based on the input
	@Test
	public void testReturnValueDependentOnMethodParameter() {
		Comparable<String> c = mock(Comparable.class);

		// define
		when(c.compareTo("Mockito")).thenReturn(1);
		when(c.compareTo("Eclipse")).thenReturn(2);

		// assert
		assertEquals(1, c.compareTo("Mockito"));
	}

	// this test demonstrates how to return values independent of the input value
	@Test
	public void testReturnValueInDependentOnMethodParameter() {
		Comparable<Integer> c = mock(Comparable.class);
		when(c.compareTo(Mockito.anyInt())).thenReturn(-1);
		// assert
		assertEquals(-1, c.compareTo(1454516));
	}

	// return a value based on the type of the provide parameter
//	@Test
//	public void testReturnValueInDependentOnMethodParameter2()  {
//	        Comparable<Todo> c= mock(Comparable.class);
//	        when(c.compareTo(isA(Todo.class))).thenReturn(0);
//	        //assert
//	        assertEquals(0, c.compareTo(new Todo(1)));
//	}

	// throw a exeption

	@Test
	public void test() {
		Properties properties = mock(Properties.class);

		when(properties.get("Anddroid")).thenThrow(new IllegalArgumentException());

		try {
			properties.get("Anddroid");
			fail("Anddroid is misspelled");
		} catch (IllegalArgumentException ex) {
			// good!
		}
	}

	@Test
	public void test2() {
		Properties properties = new Properties();

		Properties spyProperties = spy(properties);

		doReturn("42").when(spyProperties).get("shoeSize");

		String value = (String) spyProperties.get("shoeSize");

		assertEquals("42", value);
	}

	@Ignore
	@Test
	public void testLinkedListSpyWrong() {
		// Lets mock a LinkedList
		List<String> list = new LinkedList<>();
		List<String> spy = spy(list);

		// this does not work
		// real method is called so spy.get(0)
		// throws IndexOutOfBoundsException (list is still empty)
		when(spy.get(0)).thenReturn("foo");

		assertEquals("foo", spy.get(0));
	}

	@Test
	public void testLinkedListSpyCorrect() {
		// Lets mock a LinkedList
		List<String> list = new LinkedList<>();
		List<String> spy = spy(list);

		// You have to use doReturn() for stubbing
		doReturn("foo").when(spy).get(0);

		assertEquals("foo", spy.get(0));
	}
	
	@Test
	public void testVerify()  {
	    // create and configure mock
	    Location test = Mockito.mock(Location.class);

	    when(test.getLocation_id()).thenReturn(testUuid);

	    // call method testing on the mock with parameter 12
	    test.setLocation_id(testUuid);
	    test.getCountry();
	    test.getCountry();
	    test.getCreatedAt();
	    test.getCreatedAt();
	    test.getCreatedAt();
	    test.getCreatedAt();
	    test.getCreatedAt();
	    test.setCountry("country");
	    test.getModifiedAt();
	    

	    // now check if method testing was called with the parameter 12
	    //kiểm tra phương thức set Location đã được gọi vs tham số là testUuid
	    verify(test).setLocation_id(ArgumentMatchers.eq(testUuid));

	    // was the method called twice?
	    // kiểm tra xem phương thức này đã được gọi 2 lần
	    verify(test, times(2)).getCountry();

	    // other alternatives for verifiying the number of method calls for a method
	    verify(test, never()).setCity("city 2");
	    verify(test, atLeastOnce()).setCountry("country");
	    verify(test, atLeast(2)).getCountry();
	    verify(test, times(5)).getCreatedAt();
	    verify(test, atMost(3)).getModifiedAt();
	    // This let's you check that no other methods where called on this object.
	    // You call it after you have verified the expected method calls.
	    verifyNoMoreInteractions(test);
	}
}
