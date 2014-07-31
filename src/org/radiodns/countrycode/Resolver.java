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

package org.radiodns.countrycode;

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
 * This class enables the resolution of the Global Country Code (GCC) for a
 * radio service, useful in discovering RadioDNS services.
 * 
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 1.0
 */
public class Resolver {
	
	private Map<String, Country> mCountryLookupTable = new HashMap<String, Country>();
	private Map<String, Country> mGCCLookupTable = new HashMap<String, Country>();
	
	private String mIsoCountryCode = null;
	private String mEcc = null;
	private String mDabSId = null;
	private String mRdsPi = null;

	public Resolver() {
		// parse countries csv table
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					this.getClass().getResourceAsStream("countries.csv")));

			String nextLine;
			while ((nextLine = reader.readLine()) != null) {
				String[] vals = new String[5];
				String[] lineParts = nextLine.split(",");
				System.arraycopy(lineParts, 0, vals, 0, lineParts.length);

				Country country = new Country(vals[1], vals[2],
						csvArrayToList(vals[3]), csvArrayToList(vals[4]));
				// initialise a hashmap keyed on ISO Country Code
				mCountryLookupTable.put(country.getISOCountryCode(), country);
				// initialise a hashmap keyed on a country's GCCs
				for (String countryId : country.getCountryIds()) {
					mGCCLookupTable.put(countryId + country.getECC(), country);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the ISO Country Code representing the current physical location of
	 * the device
	 * 
	 * @param isoCountryCode 	ISO 3166 two-letter country code
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		// input validation
		if (isoCountryCode == null || !isoCountryCode.matches("(?i)^[A-Z]{2}$")) {
			throw new IllegalArgumentException(
					"Invalid country code. Must be an ISO 3166-1 alpha-2 country code");
		}
		mEcc = null;
		mIsoCountryCode = isoCountryCode;
	}

	/**
	 * Set the Extended Country Code (ECC) received from broadcast
	 * 
	 * @param ecc	Extended Country Code (ECC)
	 */
	public void setExtendedCountryCode(String ecc) {
		// input validation
		if (ecc == null || !ecc.matches("(?i)^[0-9A-F]{2}$")) {
			throw new IllegalArgumentException(
					"Invalid ECC value. Value must be a valid hexadecimal Extended Country Code (ECC)");
		}
		mEcc = ecc;
		mIsoCountryCode = null;
	}
	
	/**
	 * Set the RDS Programme Identification (PI) Code obtained from an FM broadcast
	 * 
	 * @param rdsPi RDS Programme Identification (PI) Code
	 */
	public void setRdsPiCode(String rdsPi) {
		// input validation
		if (rdsPi == null || !rdsPi.matches("(?i)^[0-9A-F]{4}$")) {
			throw new IllegalArgumentException(
					"Invalid PI value. Value must be a valid hexadecimal string RDS Programme Identification (PI) Code");
		}
		mRdsPi = rdsPi;
		mDabSId = null;
	}
	
	/**
	 * Set the DAB SId obtained from a DAB broadcast
	 * 
	 * @param dabSId DAB SId
	 */
	public void setDabSId(String dabSId) {
		// input validation
		if (dabSId == null || !dabSId.matches("(?i)^[0-9A-F]{4}$|^[0-9A-F]{8}$")) {
			throw new IllegalArgumentException(
					"Invalid Service Identifier (SId) value. Must be a valid 4 or 8-character hexadecimal string");
		}
		mRdsPi = null;
		mDabSId = dabSId;
		
		if (mDabSId.length() == 8) {
			mEcc = String.valueOf(mDabSId.charAt(0)) + String.valueOf(mDabSId.charAt(1));
			mIsoCountryCode = null;
		}
	}
	
	/**
	 * Resolve the correct Global Country Code (GCC) based on the inputs
	 * available to the device. Ensures the Country Code is correct in border
	 * areas.
	 * 
	 * @return List<Result> Containing Global Country Code (GCC) of matching
	 *         countries
	 * @throws ResolutionException
	 */
	public List<Result> resolveGCC() throws ResolutionException {
		String broadcastCountryId = null;
		// select the broadcast country ID from either the RDS PI or DAB SId values
		if (mRdsPi != null) {
			broadcastCountryId = String.valueOf(mRdsPi.charAt(0));
		} else if (mDabSId != null && mDabSId.length() == 4) {
			broadcastCountryId = String.valueOf(mDabSId.charAt(0));
		} else if (mDabSId != null && mDabSId.length() == 8) {
			broadcastCountryId = String.valueOf(mDabSId.charAt(2));
		} else {
			throw new IllegalStateException(
					"RDS Programme Identification (PI) OR Service Identifier (SId) must be set before attempting to resolve");
		}
		
		// construct and return a list of results
		if (mIsoCountryCode != null) {
			return resolveGCCWithCountryCode(mIsoCountryCode, broadcastCountryId);
		} else if (mEcc != null) {
			return resolveGCCWithECC(mEcc, broadcastCountryId);
		} else {
			throw new IllegalStateException(
					"ISO Country Code OR Extended Country Code (ECC) value must be set before attempting to resolve");
		}
	}

	/**
	 * Find and return the Global Country Code (GCC) for the given Extended
	 * Country Code (ECC) and Broadcast Country Code
	 * 
	 * @param ecc 	 				Extended Country Code (ECC)
	 * @param broadcastCountryCode 	Country ID (first nibble of RDS PI Code/DAB SId)
	 * @return Result 				Containing Global Country Code (GCC) of matching country
	 * @throws ResolutionException
	 */
	private List<Result> resolveGCCWithECC(String ecc, String broadcastCountryCode) throws ResolutionException {
		// input validation
		if (ecc == null || !ecc.matches("(?i)^[0-9A-F]{2}$")) {
			throw new IllegalArgumentException(
					"Invalid ECC value. Value must be a valid hexadecimal Extended Country Code (ECC)");
		}
		
		// upper case for hashmap
		ecc = ecc.toUpperCase(Locale.ENGLISH);
		broadcastCountryCode = broadcastCountryCode.toUpperCase(Locale.ENGLISH);
		
		Country country = mGCCLookupTable.get(broadcastCountryCode + ecc);
		if (country == null) {
			throw new ResolutionException(
					"A Global Country Code (GCC) could not be resolved for the given input. No match found in lookup table");
		}
		List<Result> resultList = new ArrayList<Result>();
		resultList.add(new Result(broadcastCountryCode, ecc, country.getISOCountryCode()));
		return resultList;
	}
	
	/**
	 * Find and return the Global Country Code (GCC) results for the given Extended
	 * Country Code (ECC) and Broadcast Country Code
	 * 
	 * @param country 				Country
	 * @param broadcastCountryCode 	Country ID (first nibble of RDS PI Code/DAB SId)
	 * @return List<Result> 	Containing Global Country Code (GCC) of matching countries
	 * @throws ResolutionException
	 */
	private List<Result> resolveGCCWithCountryCode(String isoCountryCode,
			String broadcastCountryCode) throws ResolutionException {

		// input validation
		if (isoCountryCode == null || !isoCountryCode.matches("(?i)^[A-Z]{2}$")) {
			throw new IllegalArgumentException(
					"Invalid country code. Must be an ISO 3166-1 alpha-2 country code");
		}

		// upper case
		isoCountryCode = isoCountryCode.toUpperCase(Locale.ENGLISH);

		// get the Country for the given ISO Country Code
		Country reportedCountry = mCountryLookupTable.get(isoCountryCode);

		if (reportedCountry == null) {
			throw new ResolutionException(
					"The supplied ISO Country Code is not recognised");
		}

		// upper case
		broadcastCountryCode = broadcastCountryCode.toUpperCase(Locale.ENGLISH);

		List<Result> resultList = new ArrayList<Result>();

		if (compareCountryIds(reportedCountry, broadcastCountryCode)) {
			// the country id of the received RDS PI Code matches the country id
			// of the reported location, return the ISO country code
			Result result = new Result(broadcastCountryCode, reportedCountry.getECC(), reportedCountry.getISOCountryCode());
			resultList.add(result);
		} else {
			// the country id & pi code do not match. Check countries adjacent
			// to the reported country to find a match (resolving
			// border-proximity issues)
			for (String nearbyCountry : reportedCountry.getNearbyCountries()) {
				String[] countryParts = nearbyCountry.split(":");

				// compare the country id of the received broadcast country code
				// to the country id for each of the nearby countries
				if (broadcastCountryCode.equals(countryParts[0])) {
					// an adjacent country matches, add to result list
					Result result = new Result(countryParts[0], mCountryLookupTable.get(countryParts[1]).getECC(), countryParts[1]);
					resultList.add(result);
				}
			}

		}
		
		if (resultList.size() == 0) {
			throw new ResolutionException(
					"A Global Country Code (GCC) could not be resolved for the given input. No match found in lookup table");			
		}

		return resultList;
	}
	
	/**
	 * Compare the Country IDs of the given country with the supplied country ID
	 * 
	 * @param country 		Country
	 * @param countryId 	Country ID (first nibble of RDS PI Code/DAB SId)
	 * @return boolean
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
	 * Convert a semi-colon delimited list String into a List
	 * 
	 * @param array 	Semi-colon delimited list String
	 * @return List of values
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