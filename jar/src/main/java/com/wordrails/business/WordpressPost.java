package com.wordrails.business;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

public class WordpressPost {    

    public class WordpressTerms {
        public static final String TAG = "T";
        public static final String CATEGORY = "N";

        public Integer id;
        public String name;
        public String slug;
        public String type;
    }

    @SerializedName("ID")
    public Integer id;
    public String title;
    
    @SerializedName("content_raw")
    public String body;
    public String status;
    public Date date;
    public Date modified;
        
    @SerializedName("terms")
    public List<WordpressTerms> terms;

    public WordpressPost(String title, String body, String status, Date date) {
        this.title = title;
        this.body = body;
        this.status = status;
        this.date = date;
    }

}
