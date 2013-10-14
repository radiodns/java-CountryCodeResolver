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
import org.radiodns.countrycode.ResolutionException;
import org.radiodns.countrycode.Resolver;

/**
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.2
 */
public class ResolutionTests {

	/*
	 * Tests where the given PI Code matches the Country Code
	 */
	@Test
	public void testCorrectCountryCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("CH + 4479 must be CH", "CH",
				resolver.resolveCountryCode("CH", "4479"));
	}

	@Test
	public void testCorrectCountryCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("GB + C479 must be GB", "GB",
				resolver.resolveCountryCode("GB", "C479"));
	}

	@Test
	public void testCorrectCountryCode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("KR + E479 must be KR", "KR",
				resolver.resolveCountryCode("KR", "E479"));
	}

	/*
	 * Tests where the given PI Code doesn't match the Country Code but does of
	 * a country adjacent to it
	 */
	@Test
	public void testAdjacentCountryCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("CH + D479 must be DE", "DE",
				resolver.resolveCountryCode("CH", "D479"));
	}

	@Test
	public void testAdjacentCountryCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("GB + 2479 must be IE", "IE",
				resolver.resolveCountryCode("GB", "2479"));
	}

	@Test
	public void testAdjacentCountryCode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		assertEquals("KP + E479 must be KR", "KR",
				resolver.resolveCountryCode("KP", "E479"));
	}

}
