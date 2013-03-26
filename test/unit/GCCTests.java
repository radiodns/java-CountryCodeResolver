/*
 * Copyright (c) 2013 Global Radio UK Limited
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package unit;

import static org.junit.Assert.*;

import org.junit.Test;
import org.radiodns.gcc.ResolutionException;
import org.radiodns.gcc.Resolver;

/**
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.1
 */
public class GCCTests {

	/*
	 * Tests where the given PI Code matches the Country Code
	 */
	@Test
	public void testCorrectCountryCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("CH + 4479 must be 4e1", "4e1",
				resolver.getGCC("CH", "4479"));
	}

	@Test
	public void testCorrectCountryCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("GB + C479 must be ce1", "ce1",
				resolver.getGCC("GB", "C479"));
	}

	@Test
	public void testCorrectCountryCode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("KR + E479 must be ef1", "ef1",
				resolver.getGCC("KR", "E479"));
	}

	/*
	 * Tests where the given PI Code doesn't match the Country Code but does of
	 * a country adjacent to it
	 */
	@Test
	public void testAdjacentCountryCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("CH + D479 must be de0", "de0",
				resolver.getGCC("CH", "D479"));
	}

	@Test
	public void testAdjacentCountryCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("GB + 2479 must be 2e3", "2e3",
				resolver.getGCC("GB", "2479"));
	}

	@Test
	public void testAdjacentCountryCode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("KP + E479 must be ef1", "ef1",
				resolver.getGCC("KP", "E479"));
	}

}
