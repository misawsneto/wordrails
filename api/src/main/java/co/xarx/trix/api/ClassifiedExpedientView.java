package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassifiedExpedientView {
  public String day;
  public boolean isActive;
  public List<ClassifiedTimesView> times;
}