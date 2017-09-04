package co.xarx.trix.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoAreaView {
  public enum Type {
    CIRCLE, POLYGON
  }
  public Type type;
  public double radius; // meters
  public GeoPointView center;
  public List<GeoPointView> polygon;
}
