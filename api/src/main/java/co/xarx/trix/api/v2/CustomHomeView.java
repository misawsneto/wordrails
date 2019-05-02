package co.xarx.trix.api.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomHomeView {

  public String type;
  public String name;
  public String categoryId;
  public String categoryName;
  public String categorySlug;
  public boolean horizontal;
  public List<Object> items;

}
