package com.wordrails.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.StationRole;
import com.wordrails.business.Taxonomy;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;

@Path("/taxonomies")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaxonomyResource {
	
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	
	private @Autowired NetworkRepository networkRepository;
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired AccessControllerUtil accessControllerUtil;

	@Path("/networks/{networkId}/taxonomiesToEdit")
	@GET
	public Response getTaxonomiesToEdit(@PathParam("networkId") Integer networkId) throws JsonProcessingException{
		List<Taxonomy> taxonomies = null;
		
		Network network = networkRepository.findOne(networkId);
		if(network != null){
			Person personLogged = accessControllerUtil.getLoggedPerson();
			NetworkRole networkRole = networkRolesRepository.findByNetworkAndPerson(network, personLogged);
			if(networkRole != null && networkRole.admin){
				taxonomies = taxonomyRepository.findNetworkOrStationTaxonomiesByNetworkIdExcludeType(networkId, Taxonomy.STATION_AUTHOR_TAXONOMY);
			}else{
				List<StationRole> stationRoles = stationRolesRepository.findByPersonIdAndNetworkId(personLogged.id, network.id);
				List<Integer> stationsIds = new ArrayList<Integer>(stationRoles.size());
				for (StationRole stationRole : stationRoles) {
					if(stationRole.admin){
						stationsIds.add(stationRole.station.id);
					}
				}
				if(!stationsIds.isEmpty()){
					taxonomies = taxonomyRepository.findByStationsIds(stationsIds);
				}
			}
		}
		return Response.ok().entity(mapper.writeValueAsString(taxonomies)).build();
	}
}