java-CountryCodeResolver
================

Version 0.3 Beta

### Introduction

This library helps with RadioDNS lookups by resolving the Global Country Code (GCC) value required to identify a radio service being consumed.

It is common for FM broadcasts to lack RDS ECC, a field which would otherwise confirm the country of origin of the broadcast. Other information such as the physical location of the device could be used on its own, however that doesn't consider the scenario of being on or close to a country border. This library enables the accurate resolution of the country of origin by taking as inputs the RDS PI or DAB SID and the two-letter country code of the physical location of the radio device or Extended Country Code (ECC).

This is beta code which requires further work.

For more information about RadioDNS, please see the official documentation: [http://radiodns.org/documentation/](http://radiodns.org/documentation/)
 

### Getting Started
Using data received by the device, the relevant values must first be set on an instance of the `Resolver` class, before then calling `resolveGCC()`.

Methods available for setting data are as follows:

	void setIsoCountryCode(String isoCountryCode)
	
	void setExtendedCountryCode(String ecc)
	
	void setRdsPiCode(String rdsPi)
	
	void setDabSId(String dabSId)

To complete the resolution the following method should be called.

	List<Result> resolveGCC()
	
The four values which can be set are:

1. An ISO 3166 two-letter country code representing the country the radio device is physically located within. This could be obtained using GPS or cell-triangulation etc (2 character string)
2. The ECC received from broadcast (2 character hexadecimal string)
3. The RDS PI Code received from FM broadcast (4 character hexadecimal string)
4. The DAB SID received from DAB broadcast (4 or 8 character hexadecimal string) 

If a 32-bit (8 character hexadecimal) DAB SId is provided it is the only value which needs to be set.

In all other cases a combination of the first or second values, and third or fourth values must be set for a successful resolution.

The method returns a list of Result objects each of which include the Global Country Code (GCC) for the country matching the Country Code / ECC and RDS PI Code / DAB SId combination. In most cases the list will contain only one result, however more may be returned in some cases.

Example:

	Resolver resolver = new Resolver();
		
		try {
		
			Resolver resolver = new Resolver();
			resolver.setIsoCountryCode("CH");
			resolver.setRdsPiCode("4479");
			
			List<Result> resultList = resolver.resolveGCC();
			Result result = resultList.get(0);
			
			System.out.println("GCC: " + result.gcc);

		} catch (ResolutionException e) {
			e.printStackTrace();
		}

In the above example the radio device has located itself within Switzerland (CH), but the first nibble of the received RDS PI is not Switzerland's Country ID of '4'. Here the library would return a Global Country Code of 'de0', having identified the FM broadcast as a German station and assuming the radio device must be near the border. 


### Data Sources

Country ID and ECC data obtained from ETSI TS 101 756 (2009) and IEC:62106/Ed2 (2009).
ISO Country Codes and adjacent countries data obtained from Wikipedia under the Creative Commons Attribution-ShareAlike License.

Certain countries listed as adjacent to others have been removed from the CSV where deemed too distant to realistically have a chance of receiving a broadcast over the border. These countries are listed below:

| Country            | Adjacent Country Removed     |
|--------------------|------------------------------|
| Albania            | Montenegro                   |
| Algeria            | Italy                        |
| Australia          | New Zealand                  |
| Bahamas            | Cuba                         |
| Bahamas            | Haiti                        |
| Bahamas            | Turks and Caicos Islands     |
| Barbados           | France                       |
| Brazil             | France                       |
| Cayman Islands     | Colombia                     |
| Cayman Islands     | Honduras                     |
| China              | Taiwan                       |
| China              | South Korea                  |
| Colombia           | Dominican Republic           |
| Colombia           | Jamaica                      |
| Colombia           | Peru                         |
| Colombia           | Cayman Islands               |
| Cuba               | Bahamas                      |
| Cuba               | Mexico                       |
| Cuba               | United States                |
| Dominican Republic | Venezuela                    |
| Honduras           | Cayman Islands               |
| India              | Thailand                     |
| India              | Maldives                     |
| India              | Indonesia                    |
| Indonesia          | Thailand                     |
| Indonesia          | India                        |
| Indonesia          | Vietnam                      |
| Indonesia          | Philippines                  |
| Iran               | Bahrain                      |
| Italy              | Malta                        |
| Italy              | Montenegro                   |
| Japan              | North Korea                  |
| Japan              | Taiwan                       |
| Libya              | Malta                        |
| Mexico             | Cuba                         |
| Mexico             | Honduras                     |
| Mozambique         | France                       |
| New Caledonia      | Australia                    |
| New Caledonia      | Fiji                         |
| New Zealand        | Australia                    |
| New Zealand        | Tonga                        |
| New Zealand        | Fiji                         |
| Nicaragua          | Colombia                     |
| Norway             | Faroe Islands                |
| Philippines        | China                        |
| Russia             | Japan                        |
| Russia             | North Korea                  |
| Russia             | Turkey                       |
| Spain              | Western Sahara               |
| Sudan              | Saudi Arabia                 |
| Sweden             | Latvia                       |
| Turkmenistan       | Azerbaijan                   |
| United Kingdom     | Faroe Islands                |
| United Kingdom     | Norway                       |
| United States      | Bahamas                      |
| Venezuela          | Montserrat                   |
| Venezuela          | Dominican Republic           |
| Venezuela          | Saint Kitts and Nevis        |
| Venezuela          | France                       |
| Venezuela          | United States Virgin Islands |

Also of note are countries with no assigned broadcast country ID or Extended Country Code but which instead inherit them from another country. In these cases the country with country ID & ECC has been added as an adjacent to enable a successful lookup (e.g. Jersey uses the UK's broadcast country ID and ECC and so GB has been added an adjacent of JE).

### Licence

Licensed under the Apache License, Version 2.0 (the "License").
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
See the License for the specific language governing permissions and 
limitations under the License.

Copyright (c) 2013 Global Radio UK Limited