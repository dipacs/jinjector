/*
 * Copyright 2015 Dávid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eagerlogic.jinjector.shared;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dávid
 */
public class JInjectorTest {

	public JInjectorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCreateNamed() {
		System.out.println("createNamed");
		String name = "Test name";
		JInjector expResult = JInjector.createNamed(name);
		assertNotNull(expResult);
		JInjector result = JInjector.getInjector(name);
		assertEquals(expResult, result);
	}

	@Test
	public void testGetInjector() {
		System.out.println("getInjector");
		String name = "Test name";
		JInjector result = JInjector.getInjector(name);
		assertNotNull(result);
	}

	@Test
	public void testGetPackageInjector() {
		JInjector l1Injector = JInjector.createNamed("com");
		assertTrue(l1Injector == JInjector.getPackageInjector());

		JInjector l2Injector = JInjector.createNamed("com.eagerlogic");
		assertTrue(l2Injector == JInjector.getPackageInjector());

		JInjector l3Injector = JInjector.createNamed("com.eagerlogic.jinjector");
		assertTrue(l3Injector == JInjector.getPackageInjector());

		JInjector l4Injector = JInjector.createNamed("com.eagerlogic.jinjector.shared");
		assertTrue(l4Injector == JInjector.getPackageInjector());
	}

}
