package com.wordrails.api;

import com.wordrails.business.Post;
import com.wordrails.business.ServiceGenerator;
import com.wordrails.business.Station;
import com.wordrails.business.Wordpress;
import com.wordrails.business.WordpressApi;
import com.wordrails.business.WordpressConfig;
import com.wordrails.business.WordpressPost;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.WordpressRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author arthur
 */
@Path("/wp")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WordpressResource {

    private @Autowired
    PostRepository postRepository;
    private @Autowired
    StationRepository stationRepository;
    private @Autowired
    WordpressRepository wordpressRepository;

    @POST
    @Path("/createPost")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createPost(WordpressPost wpPost) throws ServletException, IOException {

        try {
            Post post = new Post();
            post.wordpressId = wpPost.id;
            post.title = wpPost.title;
            post.body = wpPost.body;
            post.state = wpPost.status;
            post.date = wpPost.date;

            postRepository.save(post);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("/updatePost")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updatePost(WordpressPost wpPost) throws ServletException, IOException {

        try {
            Post post = postRepository.findByWordpressId(wpPost.id);
            post.wordpressId = wpPost.id;
            post.title = wpPost.title;
            post.body = wpPost.body;
            post.state = wpPost.status;
            post.date = wpPost.date;

            postRepository.save(post);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response postWordpressConfig(WordpressConfig config) throws ServletException, IOException {

        boolean newWordpress = false;
		try {
			List<Station> stations = stationRepository.findAll();
			Station station = null;
            Wordpress wordpress = null;
			if (!stations.isEmpty()) {
				for (Station st : stations) {
					if (st.wordpress != null) {
						if (st.wordpress.token.equals(config.token)) {
							station = st;
                            wordpress = st.wordpress;
                            newWordpress = wordpress.domain == null;
							break;
						}
					}
				}

                if (wordpress == null) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Token is not created for this station").build();
                } else if (newWordpress) {
                    //TODO manage taxonomy to enable this
                    WordpressApi api = ServiceGenerator.createService(WordpressApi.class, config.domain, config.user, config.password);
                    saveWordpressPosts(api);
                }

				wordpress.domain = config.domain;
				wordpress.username = config.user;
                wordpress.password = config.password;
                wordpress.station = station;
				
				station.wordpress = wordpress;
                wordpressRepository.save(wordpress);
			}
		} catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity(e.getMessage()).build();
		}

		return Response.status(Response.Status.OK).build();
    }

    public void saveWordpressPosts(WordpressApi api) {
        List<WordpressPost> wpPosts = api.getPosts();

        List<Post> posts = new ArrayList<>();
        for (WordpressPost wpPost : wpPosts) {
            Post post = new Post();
            post.body = wpPost.body;
            post.state = wpPost.status;
            post.date = wpPost.date;
            post.title = wpPost.title;
            post.wordpressId = wpPost.id;

            posts.add(post);
        }

        postRepository.save(posts);
    }

}
