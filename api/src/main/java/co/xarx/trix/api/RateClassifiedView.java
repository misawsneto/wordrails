package co.xarx.trix.api;

import java.util.Date;
import java.util.List;

public class RateClassifiedView {
  public Integer id;
  public int siteId;
  public int postId;
  public int stationId;
  public float rating;
  public String comment;
  public Integer userId;
  public Integer parent;
  public String userName;
  public String userImage;
  public Date created;
  public List<RateClassifiedView> children;
}
