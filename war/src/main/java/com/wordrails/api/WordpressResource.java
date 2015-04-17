package com.wordrails.api;

import com.wordrails.business.Station;
import com.wordrails.business.Wordpress;
import com.wordrails.business.WordpressConfig;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.WordpressRepository;
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

    private @Autowired
    StationRepository stationRepository;
    private @Autowired
    WordpressRepository wordpressRepository;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response postWordpressConfig(WordpressConfig config) throws ServletException, IOException {

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
							break;
						}
					}
				}

                if (wordpress == null) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Token is not created for this station").build();
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

}
