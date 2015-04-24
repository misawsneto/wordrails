package com.wordrails.business;

import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class WordpressService {

    WordpressApi api;

    public WordpressService() {
    }

    public WordpressPost createPost(Post post, WordpressApi api) {
        WordpressPost wp = new WordpressPost(post.title, post.body, post.state, new Date());

        switch (post.state) {
            case (Post.STATE_SCHEDULED):
                wp.status = "future";
                wp.date = post.date;
                break;
            case (Post.STATE_PUBLISHED):
                wp.status = "publish";
                break;
            case (Post.STATE_DRAFT):
                wp.status = "draft";
                break;
        }

        WordpressPost returnPost = null;
        try {
            returnPost = api.createPost(wp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnPost;
    }

    public Integer deletePost(Integer wordpressId, WordpressApi api) {
        Integer status = null;
        try {
            status = api.deletePost(wordpressId).getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return status;
    }

    public WordpressPost updatePost(Post post, WordpressApi api) {
        WordpressPost wp = new WordpressPost(post.title, post.body, post.state, new Date());

        switch (post.state) {
            case (Post.STATE_SCHEDULED):
                wp.status = "future";
                wp.date = post.date;
                break;
            case (Post.STATE_PUBLISHED):
                wp.status = "publish";
                break;
            case (Post.STATE_DRAFT):
                wp.status = "draft";
                break;
        }

        WordpressPost returnPost = null;
        try {
            returnPost = api.editPost(post.wordpressId, wp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnPost;
    }
}
