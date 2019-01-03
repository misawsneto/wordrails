package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieView {

  public Integer code;
  public int position;

  public Integer postId;
  public Integer blogId;
  public Integer siteId;

  public String title;
  public String sinopse;
  public String slug;
  public String image;
  public String gender;
  public String urlImage;
  public String country;
  public int age;
  public String url;
  public int duration;
  public String actors;
  public String directors;
  public String trailer;
  public String trailerDescription;
  public boolean movie3d;


  public boolean bookmarked;
  public List<SessionView> sessions;


}
