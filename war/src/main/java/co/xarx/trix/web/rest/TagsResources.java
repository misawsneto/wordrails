package co.xarx.trix.web.rest;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.TermView;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.converter.TermConverter;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import co.xarx.trix.domain.TermPerspective;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.persistence.TermPerspectiveRepository;
import co.xarx.trix.persistence.TermRepository;
import co.xarx.trix.services.AmazonCloudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class TagsResources {
    @Context
    @Autowired
    @Qualifier("simpleMapper")
    private ObjectMapper simpleMapper;


    private @Autowired
    PostConverter postConverter;

    private @Autowired
    QueryPersistence queryPersistence;

    @GET
    @Path("/search/findPostsByTag")
    public ContentResponse<List<PostView>> findPostsByTagAndStationId(@QueryParam("tags") String tagsString, @QueryParam("stationId") Integer stationId, @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
        if(tagsString == null || !tagsString.isEmpty()){
            // TODO: throw badrequest
        }

        List<String> tags = Arrays.asList(tagsString.split(","));

        List<Post> posts = queryPersistence.findPostsByTag(tags, stationId, page, size);

        ContentResponse<List<PostView>> response = new ContentResponse<>();
        response.content = postConverter.convertToViews(posts);
        return response;
    }
}
