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

/**
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.2
 */
public class ResolutionException extends Exception {

	private static final long serialVersionUID = 3904900535745210249L;

	public ResolutionException(String msg) {
		super(msg);
	}

	public ResolutionException(String msg, Throwable t) {
		super(msg, t);
	}

}