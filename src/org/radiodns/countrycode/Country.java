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

package org.radiodns.countrycode;

import java.util.List;

/**
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.2
 */
public class Country {
	String mISOCountryCode;
	String mECC;
	List<String> mCountryIds;
	List<String> mNearbyCountries;

	public Country(String isoCountryCode, String ecc, List<String> countryId,
			List<String> nearbyCountries) {
		mISOCountryCode = isoCountryCode;
		mECC = ecc;
		mCountryIds = countryId;
		mNearbyCountries = nearbyCountries;
	}

	public String getISOCountryCode() {
		return mISOCountryCode;
	}

	public String getECC() {
		return mECC;
	}

	public List<String> getCountryIds() {
		return mCountryIds;
	}

	public List<String> getNearbyCountries() {
		return mNearbyCountries;
	}
}
