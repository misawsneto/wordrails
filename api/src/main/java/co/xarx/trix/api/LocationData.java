package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationData {
	public String zip;
  public String street;
  public String number;
  public String complement;
  public String neighborhood;
  public String city;
  public String state;
  public GeoPointView geo;
}
