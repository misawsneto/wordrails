package com.wordrails.business;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class WordpressDeserializer implements JsonDeserializer<WordpressPost> {

     @Override
     public WordpressPost deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Integer id = json.getAsJsonObject().get("ID").getAsInt();
        String title = json.getAsJsonObject().get("title").getAsString();
        String body = json.getAsJsonObject().get("content").getAsString();
        String status = json.getAsJsonObject().get("status").getAsString();
        String d = json.getAsJsonObject().get("date").getAsString();
        String m = json.getAsJsonObject().get("modified").getAsString();     
        

        SimpleDateFormat sdfDateWithTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        
        Date date, modified;
        try {
           date = sdfDateWithTime.parse(d);
           modified = sdfDateWithTime.parse(m);
        } catch (ParseException e) {
           throw new RuntimeException(e);
        }
        WordpressPost wp = new WordpressPost(title, body, status, date);
        wp.modified = modified;
        wp.id = id;
        wp.terms.tags = new HashSet<>();
        
         try {
            JsonArray postTag = json.getAsJsonObject().get("terms").getAsJsonObject().getAsJsonArray("post_tag");
            for (Iterator<JsonElement> it = postTag.iterator(); it.hasNext();) {
                JsonObject tag = (JsonObject) it.next();
                
                WordpressTerm t = new WordpressTerm();
                t.id = tag.get("ID").getAsInt();
                t.name = tag.get("name").getAsString();
                t.slug = tag.get("slug").getAsString();
                t.parent = tag.get("parent").getAsInt();
                t.type = WordpressTerm.TAG;
                
                wp.terms.tags.add(t);
            }
         } catch (Exception e) {
         }
         
        wp.terms.categories = new HashSet<>();
         try {
            JsonArray postCategories = json.getAsJsonObject().get("terms").getAsJsonObject().getAsJsonArray("category");
            for (Iterator<JsonElement> it = postCategories.iterator(); it.hasNext();) {
                JsonObject category = (JsonObject) it.next();
                
                WordpressTerm t = new WordpressTerm();
                t.id = category.get("ID").getAsInt();
                t.name = category.get("name").getAsString();
                t.slug = category.get("slug").getAsString();
                t.parent = category.get("parent").getAsInt();
                t.type = WordpressTerm.CATEGORY;
                
                wp.terms.categories.add(t);
            }
         } catch (Exception e) {
         }
        

        return wp;
    }

}