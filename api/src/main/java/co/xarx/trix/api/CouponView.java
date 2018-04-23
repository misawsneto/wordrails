package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponView {
    public String eSId;
    public Integer postId;
    public Integer stationId;
    public Long siteId;

    public String title;
    public String body;
    public String slug;
    public Date date;

    public Date useDate;
    public String user;

    public Integer classifiedId;

    public String smallImageUrl;
    public String mediumImageUrl;
    public String largeImageUrl;

    public Integer authorId;
    public String authorName;
    public String authorEmail;

    public String discountType;
    public int amount;
    public boolean freeShipping;
    public Date expiryDate;
    public int minimumSpend;
    public int maximumSpend;
    public boolean individualUse;
    public boolean excludeSalesItem;
    public int usageLimit;
    public int usageCount;

    //private Integer product;  Not using yet
    //private List<Integer> excludeProducts; Not using yet
    public List<String> productCategories;
    public List<Integer> excludeCategories;

    public String emailRestriction;
    public Integer usageLimitToXItem;
    public int usageLimitPerUser;

    public ClassifiedView classified;
}
