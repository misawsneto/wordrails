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

        WordpressPost returnPost = api.createPost(wp);

        return returnPost;
    }

    public Integer deletePost(Integer wordpressId, WordpressApi api) {
        return api.deletePost(wordpressId).getStatus();
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

        WordpressPost returnPost = api.editPost(post.wordpressId, wp);

        return returnPost;
    }
}
