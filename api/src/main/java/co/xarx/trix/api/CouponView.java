package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponView {
    public String eSId;
    Integer postId;
    Integer stationId;
    Long siteId;

    String title;
    String body;
    Date date;

    Date useDate;
    String user;

    Integer classifiedId;

    String smallImageUrl;
    String mediumImageUrl;
    String largeImageUrl;

    Integer authorId;
    String authorName;
    String authorEmail;

    String discountType;
    int amount;
    boolean freeShipping;
    Date expiryDate;
    int minimumSpend;
    int maximumSpend;
    boolean individualUse;
    boolean excludeSalesItem;
    int usageLimit;
    int usageCount;

    //private Integer product;  Not using yet
    //private List<Integer> excludeProducts; Not using yet
    List<String> productCategories;
    List<Integer> excludeCategories;

    String emailRestriction;
    Integer usageLimitToXItem;
    int usageLimitPerUser;

    ClassifiedView classified;
    LocationData location;
}
