package com.wordrails.business;

import com.wordrails.persistence.NetworkRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit.client.Response;


@Service
public class WordpressService {
    
    private @Autowired
    NetworkRepository networkRepository;
    
    WordpressApi api;

    public WordpressService() {
    }
    
    
    
    
    public WordpressPost createPost(Post post) {
        List<Network> networks = networkRepository.findAll();
        if (!networks.isEmpty()) {
            Network network = networks.get(0);

            if (!network.wordpressDomain.isEmpty()) {
                api = ServiceGenerator.createService(WordpressApi.class, network.wordpressDomain, network.wordpressUsername, network.wordpressPassword);
                
                WordpressPost wp = new WordpressPost(post.title, post.body, post.state, new Date());
                
                switch(post.state) {
                    case(Post.STATE_SCHEDULED) :
                        wp.status = "future";
                        wp.date = post.date;
                        break;
                    case(Post.STATE_PUBLISHED):
                        wp.status = "publish";
                        break;
                    case(Post.STATE_DRAFT):
                        wp.status = "draft";
                        break;
                }
                
                WordpressPost returnPost = api.createPost(wp);
                

                return returnPost;
            }
        }
        
        return null;
    }

    Response deletePost(Post post) {
        
        List<Network> networks = networkRepository.findAll();
        if (!networks.isEmpty()) {
            Network network = networks.get(0);

            if (!network.wordpressDomain.isEmpty()) {
                api = ServiceGenerator.createService(WordpressApi.class, network.wordpressDomain, network.wordpressUsername, network.wordpressPassword);
                
                return api.deletePost(post.wordpressId);
            }
        }
        
        return null;
    }

    WordpressPost updatePost(Post post) {
        
        List<Network> networks = networkRepository.findAll();
        if (!networks.isEmpty()) {
            Network network = networks.get(0);

            if (!network.wordpressDomain.isEmpty()) {
                api = ServiceGenerator.createService(WordpressApi.class, network.wordpressDomain, network.wordpressUsername, network.wordpressPassword);
                
                WordpressPost wp = new WordpressPost(post.title, post.body, post.state, new Date());
                
                switch(post.state) {
                    case(Post.STATE_SCHEDULED) :
                        wp.status = "future";
                        wp.date = post.date;
                        break;
                    case(Post.STATE_PUBLISHED):
                        wp.status = "publish";
                        break;
                    case(Post.STATE_DRAFT):
                        wp.status = "draft";
                        break;
                }
                
                WordpressPost returnPost = api.editPost(post.wordpressId, wp);
                

                return returnPost;
            }
        }
        
        return null;
    }
}
