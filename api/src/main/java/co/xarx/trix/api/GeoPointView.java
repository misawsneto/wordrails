package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoPointView {
	public double lat;
  public double lng;
}
