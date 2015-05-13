package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

public class WordpressTerms {
    
    @JsonProperty("post_tag")
    public Set<WordpressTerm> tags;
    @JsonProperty("category")
    public Set<WordpressTerm> categories;
}