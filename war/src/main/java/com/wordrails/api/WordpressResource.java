package com.wordrails.api;

import com.wordrails.business.Network;
import com.wordrails.business.WordpressConfig;
import com.wordrails.persistence.NetworkRepository;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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
    
	private @Autowired NetworkRepository networkRepository;
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response postWordpressConfig(WordpressConfig config) throws ServletException, IOException {
        
        try {
            List<Network> networks = networkRepository.findAll();
                if(!networks.isEmpty()){
                    Network network = networks.get(0);
                    
                    if(!config.token.equals(network.wordpressToken)) {
                        return Response.status(Response.Status.UNAUTHORIZED).build();
                    }
                    
                    network.wordpressDomain = config.domain;
                    network.wordpressUsername = config.user;
                    network.wordpressPassword = config.password;
                    networkRepository.save(network);
                }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        
        return Response.status(Response.Status.OK).build();
    }
    
}
