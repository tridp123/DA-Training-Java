package com.springjpa.unitTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/*In JUnit, you can run multiple test cases with @RunWith and @Suite annotation*/
@RunWith(Suite.class)
@SuiteClasses(value = { LocationControllerTest.class, LocationRepositoryTest.class, LocationServiceTest.class })
public class UnitTestSuite {
}
