package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class WordpressPost {

    @JsonProperty("ID")
    @SerializedName("ID")
    public Integer id;
    public String title;
    
    @JsonProperty("content")
    @SerializedName("content")
    public String body;
    public String status;
    public String slug;
    public Date date;
    public Date modified;
    
    @JsonProperty("terms")
    @SerializedName("terms")
    public WordpressTerms terms;

    public WordpressPost() {
    }

    public WordpressPost(String title, String body, String status, Date date) {
        this.title = title;
        this.body = body;
        this.status = status;
        this.date = date;
    }

}
