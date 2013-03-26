java-gccresolver
================

Version 0.1 Beta

### Introduction

This library helps with RadioDNS lookups by providing the Global Country Code (GCC) required to identify an FM radio service being consumed.

It is common for FM broadcasts to lack RDS ECC, a field required to form the GCC. This library enables the resolution of the GCC taking as inputs the RDS PI and the two-letter country code of the physical location of the radio device.

This is beta code which requires further work.

For more information about RadioDNS, please see the official documentation: [http://radiodns.org/documentation/](http://radiodns.org/documentation/)


### Getting Started

The library has one method:

	String getGCC(String isoCountryCode, String piCode) throws ResolutionException

The method takes two arguments:

1. An ISO 3166 two-letter country code representing the country the radio device is physically located within. This could be obtained using GPS or cell-triangulation etc.
2. The RDS PI Code received from the FM broadcast.

Given the two arguments the method will return the GCC taking into account border situations where the radio device may be in one country whilst receiving a broadcast originating from another. The library does this by comparing the first nibble of the received RDS PI Code with the Country ID assigned to nearby countries.

Example:

	Resolver resolver = new Resolver();
		
		try {

			String gcc = resolver.getGCC("CH", "D479");		

			System.out.println("GCC: " + gcc);
			
		} catch (ResolutionException e) {
			e.printStackTrace();
		}

In the above example the radio device has located itself within Switzerland (CH), but the first nibble of the received RDS PI is not Switzerland's Country ID of '4'. Here the library would return a GCC of 'de0', having identified the FM broadcast as a German station and assuming the radio device must be near the border. 


### Data Sources

Country ID and ECC data obtained from ETSI TS 101 756 (2009) and IEC:62106/Ed2 (2009).
ISO Country Codes and adjacent countries data obtained from Wikipedia under the Creative Commons Attribution-ShareAlike License.


### Licence

Licensed under the Apache License, Version 2.0 (the "License").
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
See the License for the specific language governing permissions and 
limitations under the License.

Copyright (c) 2012 Global Radio UK Limited