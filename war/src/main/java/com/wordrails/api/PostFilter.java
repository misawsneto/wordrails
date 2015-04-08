package com.wordrails.api;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.WordrailsUtil;
import com.wordrails.business.Network;
import com.wordrails.business.Post;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.WordpressApi;
import com.wordrails.business.WordpressPost;
import com.wordrails.business.WordpressService;
import com.wordrails.persistence.PostRepository;

@Component
public class PostFilter implements Filter {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WordrailsUtil wordrailsUtil;

    @Autowired
    private WordpressService wordpressService;

    @Override
    public void destroy() {/*not implemented*/

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {/*not implemented*/

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
        FilterChain fc) throws IOException, ServletException {
        // before filter are applied
        fc.doFilter(req, resp);
        // after filters are applied
        HttpServletRequest rq = (HttpServletRequest) req;
        if (rq.getMethod().toLowerCase().equals("post")) {
            HttpServletResponse res = (HttpServletResponse) resp;
            Integer postId;
            try {
                postId = getPostId(res.getHeader("Location"));
                handleAfterCreate(postId, req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (rq.getMethod().toLowerCase().equals("put")) {
            HttpServletResponse res = (HttpServletResponse) resp;
            Integer postId;
            try {
                postId = getPostId(res.getHeader("Location"));
                handleAfterUpdate(postId, req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (rq.getMethod().toLowerCase().equals("delete")) {
            HttpServletResponse res = (HttpServletResponse) resp;
            Integer postId;
            try {
                postId = getPostId(res.getHeader("Location"));
                handleAfterDelete(postId, req);
            } catch (Exception e) {
                e.printStackTrace();
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
    private void handleAfterCreate(Integer postId, ServletRequest req) throws Exception {
        Post post = postRepository.findOne(postId);
        Network network = wordrailsUtil.getNetworkFromHost(req);
        WordpressApi api = ServiceGenerator.createService(WordpressApi.class, network.wordpressDomain, network.wordpressUsername, network.wordpressPassword);

        WordpressPost wpPost = wordpressService.createPost(post, api);

        post.wordpressId = wpPost.id;
        postRepository.save(post);
    }

    @Async
    @Transactional
    private void handleAfterUpdate(Integer postId, ServletRequest req) throws Exception {
        Post post = postRepository.findOne(postId);
        Network network = wordrailsUtil.getNetworkFromHost(req);
        WordpressApi api = ServiceGenerator.createService(WordpressApi.class, network.wordpressDomain, network.wordpressUsername, network.wordpressPassword);

        wordpressService.updatePost(post, api);
    }

    @Async
    @Transactional
    private void handleAfterDelete(Integer postId, ServletRequest req) throws Exception {
        Network network = wordrailsUtil.getNetworkFromHost(req);
        WordpressApi api = ServiceGenerator.createService(WordpressApi.class, network.wordpressDomain, network.wordpressUsername, network.wordpressPassword);

        wordpressService.deletePost(postId, api);
    }

}
