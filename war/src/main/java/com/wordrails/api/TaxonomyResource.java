package com.wordrails.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	private @Autowired NetworkRepository networkRepository;
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired TrixAuthenticationProvider authProvider;
	private @Autowired WordrailsService wordrailsService;
	public @Autowired @Qualifier("objectMapper")
	ObjectMapper mapper;

	@Path("/networks/{networkId}/taxonomiesToEdit")
	@GET
	public Response getTaxonomiesToEdit(@PathParam("networkId") Integer networkId) throws JsonProcessingException{
		List<Taxonomy> taxonomies = null;
		
		Network network = networkRepository.findOne(networkId);
		if(network != null){
			Person personLogged = authProvider.getLoggedPerson();
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

	@Path("/allCategories")
	@GET
	public List<Taxonomy> getCategories(@Context HttpServletRequest request, @QueryParam("stationId") Integer stationId) throws JsonProcessingException {
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		Taxonomy category = null;
		List<Taxonomy> taxonomies = taxonomyRepository.findNetworkCategories(network.id);
		if(taxonomies != null && taxonomies.size() > 0){
			category = taxonomies.get(0);
		}

		List<Term> categoryTerms = new ArrayList<Term>();
		for(Term term: category.terms){
			if(term.parent == null)
				categoryTerms.add(term);

			term.cells = null; term.children = null; term.parent = null; term.posts = null; term.rows = null; term.termPerspectives = null; term.taxonomy = null;
		}

		Taxonomy scategory = null;
		List<Taxonomy> staxonomies = taxonomyRepository.findStationTaxonomy(stationId);
		if(staxonomies != null && staxonomies.size() > 0){
			scategory = staxonomies.get(0);
		}

		List<Term> scategoryTerms = new ArrayList<Term>();
		for(Term term: scategory.terms){
			if(term.parent == null)
				scategoryTerms.add(term);
			term.cells = null; term.children = null; term.parent = null; term.posts = null; term.rows = null; term.termPerspectives = null; term.taxonomy = null;
		}

		ArrayList<Taxonomy> allTax = new ArrayList<Taxonomy>();

		category.owningNetwork = null; category.owningStation = null; category.networks = null;
		scategory.owningNetwork = null; scategory.owningStation = null; scategory.networks = null;

		category.terms = new HashSet<Term>(categoryTerms);
		scategory.terms = new HashSet<Term>(scategoryTerms);

		allTax.add(category);
		allTax.add(scategory);

		//return Response.status(Response.Status.OK).entity("{\"taxonomies\": " + mapper.writeValueAsString(allTax) +"}").build();
		return  allTax;
	}
}