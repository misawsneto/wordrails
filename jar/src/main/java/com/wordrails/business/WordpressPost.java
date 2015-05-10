package com.wordrails.business;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.Set;

public class WordpressPost {

    @SerializedName("ID")
    public Integer id;
    public String title;
    
    @SerializedName("content_raw")
    public String body;
    public String status;
    public Date date;
    public Date modified;
        
    public Set<WordpressTerm> tags;        
    public Set<WordpressTerm> categories;

    public WordpressPost(String title, String body, String status, Date date) {
        this.title = title;
        this.body = body;
        this.status = status;
        this.date = date;
    }

}
