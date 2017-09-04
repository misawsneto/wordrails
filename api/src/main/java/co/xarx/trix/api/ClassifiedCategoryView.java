package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassifiedCategoryView {
  int id;
  String name;
}
