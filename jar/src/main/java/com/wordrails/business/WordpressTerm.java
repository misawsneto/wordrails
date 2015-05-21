package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author arthur
 */
public class WordpressTerm {

    public static final String TAG = "post_tag";
    public static final String CATEGORY = "category";

    @JsonProperty("ID")
    @SerializedName("ID")
    public Integer id;
    public String name;
    public String slug;
    public Integer parent;
    
    @JsonProperty("trix_id")
    @SerializedName("trix_id")
    public Integer trixId;
    
    @JsonProperty("taxonomy")
    @SerializedName("taxonomy")
    public String type;
    
    public boolean isTag() {
        return type.equals(TAG);
    }
    
    public boolean isCategory() {
        return type.equals(CATEGORY);
    }
}
