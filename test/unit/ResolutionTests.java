/*
 * Copyright (c) 2014 Global Radio UK Limited
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.radiodns.countrycode.ResolutionException;
import org.radiodns.countrycode.Resolver;
import org.radiodns.countrycode.Result;

/**
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 1.0
 */
public class ResolutionTests {

	/*
	 * Tests where the given PI Code matches the Country Code
	 */
	@Test
	public void testCorrectCountryCodeWithPI1() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("4", "E1", "CH"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("CH");
		resolver.setRdsPiCode("4479");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("CH + 4479 must return one result of 4e1", expectedResult, actualResult);
	}

	@Test
	public void testCorrectCountryCodeWithPI2() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("C", "E1", "GB"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("GB");
		resolver.setRdsPiCode("C479");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("GB + C479 must return one result of ce1", expectedResult, actualResult);
	}

	@Test
	public void testCorrectCountryCodeWithPI3() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("A", "E0", "AT"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("AT");
		resolver.setRdsPiCode("A479");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("AT + 5479 must return one result of ae0", expectedResult, actualResult);
	}
	
	/*
	 * Tests where the given SID Code matches the Country Code
	 */
	@Test
	public void testCorrectCountryCodeWithSID1() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("C", "E1", "GB"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("GB");
		resolver.setDabSId("C479");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("GB + C479 must return one result of ce1", expectedResult, actualResult);
	}
	
	@Test
	public void testCorrectCountryCodeWith32bitSID1() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("C", "E1", "GB"));
		
		Resolver resolver = new Resolver();
		resolver.setDabSId("E1C00098");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("E1C00098 must return one result of ce1", expectedResult, actualResult);
	}

	/*
	 * Tests where the given PI Code doesn't match the Country Code but does of
	 * a country or countries adjacent to it
	 */
	@Test
	public void testAdjacentCountryCodeWithPI1() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("D", "E0", "DE"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("ch");
		resolver.setRdsPiCode("d479");
		List<Result> actualResult = resolver.resolveGCC();		
		assertEquals("CH + D479 must return one result of de0", expectedResult, actualResult);
	}

	@Test
	public void testAdjacentCountryCodeWithPI2() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("2", "E3", "IE"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("GB");
		resolver.setRdsPiCode("2479");
		List<Result> actualResult = resolver.resolveGCC();	
		assertEquals("GB + 2479 must return one result of 2e3", expectedResult, actualResult);
	}

	@Test
	public void testAdjacentCountryCodeWithPI3() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("5", "E0", "IT"));
		expectedResult.add(new Result("5", "E2", "SK"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("AT");
		resolver.setRdsPiCode("5479");
		List<Result> actualResult = resolver.resolveGCC();	
		assertEquals("AT + 5479 must return two results of 5e0 & 5e2", expectedResult, actualResult);
	}
	
	/*
	 * Tests where the given SID Code doesn't match the Country Code but does of
	 * a country or countries adjacent to it
	 */
	@Test
	public void testAdjacentCountryCodeWithSID1() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("D", "E0", "DE"));
		
		Resolver resolver = new Resolver();
		resolver.setIsoCountryCode("CH");
		resolver.setDabSId("D479");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("CH + D479 must return one result of de0", expectedResult, actualResult);
	}
	
	/*
	 * Tests resolving a GCC with an ECC
	 */
	@Test
	public void testECCWithPI1() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("D", "E0", "DE"));
		
		Resolver resolver = new Resolver();
		resolver.setExtendedCountryCode("E0");
		resolver.setRdsPiCode("D479");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("E0 + D479 must return one result of de0", expectedResult, actualResult);
	}	
	
	@Test
	public void testECCWithSID1() throws ResolutionException {
		List<Result> expectedResult = new ArrayList<Result>(); 
		expectedResult.add(new Result("D", "E0", "DE"));
		
		Resolver resolver = new Resolver();
		resolver.setExtendedCountryCode("E0");
		resolver.setDabSId("D479");
		List<Result> actualResult = resolver.resolveGCC();
		assertEquals("E0 + D479 must return one result of de0", expectedResult, actualResult);
	}

}
