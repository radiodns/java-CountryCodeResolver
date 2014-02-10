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

import org.junit.Test;
import org.radiodns.countrycode.ResolutionException;
import org.radiodns.countrycode.Resolver;

/**
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.2
 */
public class ExceptionTests {

	
	/*
	 * Invalid Country Code
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountryCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode(null, "C479");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountryCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("X", "C479");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountryCode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("XXX", "C479");
	}
	
	/*
	 * Country Code not found in lookup table
	 */
	@Test(expected = ResolutionException.class)
	public void testUnknownCountryCode() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("XX", "C479");
	}
	
	/*
	 * Invalid ECC
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidECC1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromECC(null, "c479");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidECC2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromECC("", "c479");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidECC3() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromECC("X", "c479");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidECC4() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromECC("XX", "c479");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidECC5() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromECC("XXX", "c479");
	}
	
	/*
	 * ECC not found in lookup table
	 */
	@Test(expected = ResolutionException.class)
	public void testUnknownECC() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromECC("A0", "c479");
	}
	
	
	/*
	 * Invalid PI Code
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("CH", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("CH", "A");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("CH", "AAAAA");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode4() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("CH", "XXXX");
	}

	/*
	 * Country Code and PI Code combination which doesn't resolve a country code
	 */
	@Test(expected = ResolutionException.class)
	public void testUnadjacentPICode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromCountryCode("CH", "B479");
	}

	/*
	 * ECC and PI Code combination which doesn't resolve a country code
	 */
	@Test(expected = ResolutionException.class)
	public void testUnadjacentPICode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.resolveCountryCodeFromECC("E4", "D479");
	}
	
}
