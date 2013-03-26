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

package org.radiodns.gcc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Given the ISO 3166 two-letter country code for a listener's current physical
 * location and the received RDS PI Code from an FM broadcast, this class will
 * enable the resolution of the Global Country Code (GCC), required to discover
 * RadioDNS services available for a given station.
 * 
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.1
 */
public class Resolver {

	Map<String, Country> mCountryLookupTable = new HashMap<String, Country>();

	public Resolver() {
		// parse countries csv table
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("countries.csv")));

			String nextLine;
			while ((nextLine = reader.readLine()) != null) {
				String[] vals = new String[4];
				String[] lineParts = nextLine.split(",");
				System.arraycopy(lineParts, 0, vals, 0, lineParts.length);

				Country country = new Country(vals[0], vals[1],
						csvArrayToList(vals[2]), csvArrayToList(vals[3]));
				mCountryLookupTable.put(country.getISOCountryCode(), country);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resolve the Global Country Code (GCC) based on the ISO 3166 two-letter
	 * country code of the listener's physical location and the received RDS PI
	 * Code from an FM broadcast
	 * 
	 * @param isoCountryCode 	ISO 3166 two-letter country code
	 * @param piCode 			RDS PI Code
	 * @return					Global Country Code (GCC)
	 * @throws ResolutionException
	 */
	public String getGCC(String isoCountryCode, String piCode)
			throws ResolutionException {

		// input validation
		if (isoCountryCode == null || !isoCountryCode.matches("(?i)^[A-Z]{2}$")) {
			throw new IllegalArgumentException(
					"Invalid country code. Must be an ISO 3166-1 alpha-2 country code");
		}

		if (piCode == null || !piCode.matches("(?i)^[0-9A-F]{4}$")) {
			throw new IllegalArgumentException(
					"Invalid PI value. Value must be a valid hexadecimal RDS Programme Identifier (PI) code");
		}

		// take the first nibble of the pi code to get the country Id
		String piCountryId = String.valueOf(piCode.charAt(0));

		// enforce lowercase; the lookup table is all lowercase
		piCountryId = piCountryId.toLowerCase(Locale.ENGLISH);
		isoCountryCode = isoCountryCode.toLowerCase(Locale.ENGLISH);

		// get the Country for the given ISO Country Code
		Country reportedCountry = mCountryLookupTable.get(isoCountryCode);

		if (reportedCountry == null) {
			throw new ResolutionException(
					"The supplied ISO Country Code is not recognised");
		}

		if (compareCountryIds(reportedCountry, piCountryId)) {
			// the country id of the received RDS PI Code matches the country id
			// of the reported location, return the GCC
			return createGCC(piCountryId, reportedCountry.getECC());
		} else {
			// the country id & pi code do not match. Check countries adjacent
			// to the reported country to find a match (resolving
			// border-proximity issues)
			for (String countryCode : reportedCountry.getNearbyCountries()) {
				Country country = mCountryLookupTable.get(countryCode);

				if (compareCountryIds(country, piCountryId)) {
					// an adjacent country matches, return the GCC
					return createGCC(piCountryId, country.getECC());
				}
			}

			throw new ResolutionException(
					"A GCC could not be resolved for the given ISO Country Code and PI Code. No match found in lookup table");
		}
	}
	
	/**
	 * Compare the Country IDs of the given country with the supplied country ID
	 * 
	 * @param country		Country
	 * @param countryId 	Country ID (first nibble of RDS PI Code)
	 * @return
	 */
	private boolean compareCountryIds(Country country, String countryId) {
		for (String id : country.getCountryIds()) {
			if (countryId.equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create the Global Country Code (GCC) - The Country ID (first nibble of
	 * the RDS PI code) concatenated with the country's RDS ECC
	 * 
	 * @param countryId 	Country ID (first nibble of RDS PI Code)
	 * @param ecc 			RDS ECC
	 * @return				Global Country Code (GCC)
	 */
	private String createGCC(String countryId, String ecc) {
		return countryId + ecc;
	}
	
	/**
	 * Convert a semi-colon delimited list String into a List
	 * 
	 * @param array		Semi-colon delimited list String
	 * @return			List of values
	 */
	private List<String> csvArrayToList(String array) {
		List<String> result = new ArrayList<String>();
		if (array == null || array.equals("")) {
			// return empty array
			return result;
		}
		String[] parts = array.split(";");
		Collections.addAll(result, parts);
		return result;
	}
}