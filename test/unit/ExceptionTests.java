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
 * @version 0.3
 */
public class ExceptionTests {

	/*
	 * Invalid Country Code
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountryCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountryCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("X");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCountryCode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("XXX");
	}
	
	/*
	 * Invalid ECC
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidECC1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setExtendedCountryCode(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidECC2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setExtendedCountryCode("XX");
	}
	
	/*
	 * Country Code not found in lookup table
	 */
	@Test(expected = ResolutionException.class)
	public void testUnknownCountryCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("XX");
		resolver.setRdsPiCode("C479");
		resolver.resolveGCC();
	}
	
	@Test(expected = ResolutionException.class)
	public void testUnknownCountryCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("XX");
		resolver.setDabSId("C479");
		resolver.resolveGCC();
	}
	
	/*
	 * ECC/Broadcast Country Code not found in lookup table
	 */
	@Test(expected = ResolutionException.class)
	public void testUnknownECC1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setExtendedCountryCode("AA");
		resolver.setRdsPiCode("C479");
		resolver.resolveGCC();
	}
	
	@Test(expected = ResolutionException.class)
	public void testUnknownECC2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setExtendedCountryCode("AA");
		resolver.setDabSId("C479");
		resolver.resolveGCC();
	}
	
	/*
	 * Invalid PI Code
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setRdsPiCode(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setRdsPiCode("A");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode3() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setRdsPiCode("AAAAA");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidPICode4() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setRdsPiCode("XXXX");
	}
	
	/*
	 * Invalid SID
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidSIDCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setDabSId(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidSIDCode2() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setDabSId("AAAAA");
	}

	/*
	 * Country Code and PI Code combination which doesn't resolve a country code
	 */
	@Test(expected = ResolutionException.class)
	public void testUnadjacentPICode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("CH");
		resolver.setRdsPiCode("B479");
		resolver.resolveGCC();
	}
	
	/*
	 * Country Code and SID Code combination which doesn't resolve a country code
	 */	
	@Test(expected = ResolutionException.class)
	public void testUnadjacentSIDCode1() throws ResolutionException {
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("CH");
		resolver.setDabSId("B479");
		resolver.resolveGCC();
	}
	
}
