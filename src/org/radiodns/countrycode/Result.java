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

import java.util.Locale;

/**
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.3
 */
public class Result {
	public String broadcastCountryCode;
	public String ecc;
	public String isoCountryCode;	
	public String gcc;
	
	public Result(String broadcastCountryCode, String ecc, String isoCountryCode) {
		this.broadcastCountryCode = broadcastCountryCode;
		this.ecc = ecc;
		this.isoCountryCode = isoCountryCode;
		this.gcc = (this.broadcastCountryCode + this.ecc).toLowerCase(Locale.ENGLISH);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.broadcastCountryCode.equals(((Result) obj).broadcastCountryCode) &&
				this.ecc.equals(((Result) obj).ecc) &&
				this.isoCountryCode.equals(((Result) obj).isoCountryCode) &&
				this.gcc.equals(((Result) obj).gcc));
	}
	
	@Override
	public int hashCode() {
		return 37 * (this.broadcastCountryCode.hashCode() + this.ecc.hashCode() + this.isoCountryCode.hashCode() + this.gcc.hashCode()) + 3;
	}
}
