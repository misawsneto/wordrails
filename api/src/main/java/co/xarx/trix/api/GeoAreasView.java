package co.xarx.trix.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoAreasView {
  public List<GeoAreaView> geoAreas;
  public boolean following;
}
