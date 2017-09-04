package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassifiedTimesView {
	public String timeFrom;
  public String timeTill;
}
