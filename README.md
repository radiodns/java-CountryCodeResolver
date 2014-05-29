java-CountryCodeResolver
================

Version 0.2 Beta

### Introduction

This library helps with RadioDNS lookups by confirming the ISO 3166 two-letter country code required to identify an FM radio service being consumed.

It is common for FM broadcasts to lack RDS ECC, a field which would otherwise confirm the country of origin of the broadcast. Other information such as the physical location of the device could be used on its own, however that doesn't consider the scenario of being on or close to a country border. This library enables the accurate resolution of the country of origin by taking as inputs the RDS PI and the two-letter country code of the physical location of the radio device. It also enables the resolution of Country Code for a given RDS ECC and RDS PI code combination.

This is beta code which requires further work.

For more information about RadioDNS, please see the official documentation: [http://radiodns.org/documentation/](http://radiodns.org/documentation/)
 

### Getting Started
The library has two methods.

	String resolveCountryCodeFromCountryCode(String isoCountryCode, String piCode) throws ResolutionException
	
	String resolveCountryCodeFromECC(String ecc, String piCode) throws ResolutionException
	
The three possible arguments are:

1. An ISO 3166 two-letter country code representing the country the radio device is physically located within. This could be obtained using GPS or cell-triangulation etc.
2. The RDS PI Code received from the FM broadcast.
3. The RDS Extended Country Code (ECC) value received from the FM broadcast.

The method will return an ISO 3166 two-letter country code for the country matching the Country Code/RDS ECC and RDS PI Code combination.

Example:

	Resolver resolver = new Resolver();
		
		try {

			String countryCode = resolver.resolveCountryCodeFromCountryCode("CH", "D479");		

			System.out.println("Country Code: " + countryCode);
			
		} catch (ResolutionException e) {
			e.printStackTrace();
		}

In the above example the radio device has located itself within Switzerland (CH), but the first nibble of the received RDS PI is not Switzerland's Country ID of '4'. Here the library would return a country code of 'DE', having identified the FM broadcast as a German station and assuming the radio device must be near the border. 


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