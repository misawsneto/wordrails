package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassifiedSectionView {

    public List<ClassifiedView> classifieds;
    public List<CouponView> coupons;

}
