package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.PostRead;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.Wordpress;
import com.wordrails.business.WordpressApi;
import com.wordrails.business.WordpressPost;
import com.wordrails.business.WordpressService;
import com.wordrails.persistence.PostReadRepository;
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
import org.springframework.scheduling.annotation.Async;
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
    private AccessControllerUtil accessControllerUtil;

    @Autowired
    private PostReadRepository postReadRepository;
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
                handleAfterRead(rq.getRequestURI());
            } else if (rq.getMethod().toLowerCase().equals("post") && res.containsHeader("Location")) {
                postId = getPostId(res.getHeader("Location"));
                Post post = postRepository.findOne(postId);
                Wordpress wp = post.station.wordpress;

                if (wp.domain != null && wp.username != null && wp.password != null) {
                    WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
                    handleAfterCreate(post, api);
                }
            } else if (NumberUtils.isNumber(urlParts[urlParts.length - 1])) {
                postId = getPostId(rq.getRequestURI());
                Post post = postRepository.findOne(postId);

                if (post != null) {
                    Wordpress wp = post.station.wordpress;

                    if (wp.domain != null && wp.username != null && wp.password != null) {
                        WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
                        switch (rq.getMethod().toLowerCase()) {
                            case "put":
                                handleAfterUpdate(post, api);
                                break;
                            case "delete":
                                handleAfterDelete(post.wordpressId, api);
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Transactional
    private void handleAfterRead(String url) throws Exception {
        if (url.matches("(.*)[/posts/[0-9]]$")) {
            Person person = accessControllerUtil.getLoggedPerson();
            Integer postId = getPostId(url);
            Post post = postRepository.findOne(postId);
            PostRead postRead = new PostRead();
            postRead.person = person;
            postRead.post = post;
            try {
                postReadRepository.save(postRead);
                wordrailsService.incrementReadsCount(post.id);
            } catch (org.springframework.dao.DataIntegrityViolationException ex) {
                log.debug("DataIntegrityViolationException " + post.id + " " + person.id + " - PostRead already exists... ");
            }
        }
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

    @Async
    @Transactional
    private void handleAfterCreate(Post post, WordpressApi api) throws Exception {
        try {
            WordpressPost wpPost = wordpressService.createPost(post, api);

            post.wordpressId = wpPost.id;
            postRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Transactional
    private void handleAfterUpdate(Post post, WordpressApi api) throws Exception {
        try {
            wordpressService.updatePost(post, api);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Transactional
    private void handleAfterDelete(Integer postId, WordpressApi api) throws Exception {
        try {
            wordpressService.deletePost(postId, api);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
