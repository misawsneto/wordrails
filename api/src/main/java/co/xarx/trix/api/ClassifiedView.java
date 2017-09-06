package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassifiedView {
	public String eSId;
  public ClassifiedPaymentMethodView paymentMethods;
  public String primaryPhone;
  public String secondaryPhone;
  public String email;
  public String facebookPage;
  public String googlePage;
  public String instagramPage;
  public String twitterPage;
  public String videoUrl;
  public String smallImageUrl;
  public String mediumImageUrl;
  public String largeImageUrl;
  public List<String> imageList;
  public List<Integer> displayInStations;
  public List<ClassifiedExpedientView> classifiedExpedients;
  public Integer totalVotes;
  public Float rating;
  public Boolean enableRating;
  public Boolean enableLocation;
  public ClassifiedLocationView location;

  public Integer postId;
  public Integer stationId;
  public Long siteId;
  public String body;
  public String title;
  public Date date;
  public int focusX;
  public int focusY;
  public String authorName;
  public String authorUsername;
  public String authorEmail;

  public List<String> tags = new ArrayList<>();
  public List<ClassifiedCategoryView> categories = new ArrayList<>();
}
