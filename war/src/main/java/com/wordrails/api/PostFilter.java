package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.business.Post;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.Wordpress;
import com.wordrails.business.WordpressApi;
import com.wordrails.business.WordpressPost;
import com.wordrails.business.WordpressService;
import com.wordrails.persistence.PostRepository;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PostFilter implements Filter {

    static Logger log = Logger.getLogger(PostFilter.class.getName());

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WordpressService wordpressService;

    @Autowired
    private WordrailsService wordrailsService;

    @Override
    public void destroy() {/* not implemented */

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {/*
         * not
         * implemented
         */

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc) throws IOException, ServletException {
        // before filter are applied
        fc.doFilter(req, resp);
        // after filters are applied
        HttpServletRequest rq = (HttpServletRequest) req;
        HttpServletResponse res = (HttpServletResponse) resp;
        Integer postId;
        try {
            String[] urlParts = rq.getRequestURI().split("/");
            log.info("POSTFILTER URL: " + rq.getMethod().toUpperCase() + " " + rq.getRequestURI());

            if (rq.getMethod().toLowerCase().equals("get")) {
                String url = rq.getRequestURI();
                if (url.contains("/posts/") && url.matches("(.*)\\d+$")) {
                    postId = getPostId(url);
                    Post post = postRepository.findOne(postId);
                    handleAfterRead(post);
                    wordrailsService.countPostRead(post, rq.getRequestedSessionId());
                }
            } else if (rq.getMethod().toLowerCase().equals("post") && res.containsHeader("Location")) {
                postId = getPostId(res.getHeader("Location"));
                Post post = postRepository.findOne(postId);
                handleAfterCreate(post);
            } else if (rq.getMethod().toLowerCase().equals("put") && NumberUtils.isNumber(urlParts[urlParts.length - 1])) {
                postId = getPostId(rq.getRequestURI());
                Post post = postRepository.findOne(postId);
                handleAfterUpdate(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAfterRead(Post post) throws Exception {

    }

    /**
     * Spring data rest adds a Location header with hyperlink representation of the newly created post
     *
     * @param location
     */
    private Integer getPostId(String location) throws Exception {
        String[] strings = location.split("/");
        return Integer.parseInt(strings[strings.length - 1]);
    }

    @Transactional
    private void handleAfterCreate(Post post) throws Exception {
        createWordpressPost(post);

    }

    @Transactional
    private void handleAfterUpdate(Post post) throws Exception {
        updateWordpressPost(post);
    }

    private void createWordpressPost(Post post) throws Exception {
        Wordpress wp = post.station.wordpress;
        if (wp != null && wp.domain != null && wp.username != null && wp.password != null) {
            WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
            WordpressPost wpPost = wordpressService.createPost(post, api);
            post.wordpressId = wpPost.id;
            postRepository.save(post);
        }
    }

    private void updateWordpressPost(Post post) throws Exception {
        if (post != null) {
            Wordpress wp = post.station.wordpress;
            if (wp != null && wp.domain != null && wp.username != null && wp.password != null) {
                WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
                wordpressService.updatePost(post, api);
            }
        }
    }

}
