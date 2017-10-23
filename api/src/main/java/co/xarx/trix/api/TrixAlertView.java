package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrixAlertView {

    public Integer id;
    public Integer stationId;

    public Long siteId;

    public Integer postId;
    public String body;

    public String smallImageUrl;
    public String mediumImageUrl;
    public String largeImageUrl;

    public Date date;

    public String authorName;
    public String authorUsername;
    public String authorEmail;
    public String authorProfileImage;
    public boolean seen;
}
