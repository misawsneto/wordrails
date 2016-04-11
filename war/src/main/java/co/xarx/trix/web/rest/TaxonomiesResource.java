package co.xarx.trix.web.rest;

import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Path("/taxonomies")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaxonomiesResource {
	@Autowired
	public ObjectMapper objectMapper;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private AuthService authProvider;

	@Path("/networks/{networkId}/taxonomiesToEdit")
	@GET
	public Response getTaxonomiesToEdit(@PathParam("networkId") Integer networkId) throws IOException {
		List<Taxonomy> taxonomies = null;

		Network network = networkRepository.findOne(networkId);
		if(network != null){
			Person personLogged = authProvider.getLoggedPerson();
			if(personLogged.networkAdmin){
				taxonomies = taxonomyRepository.findNetworkOrStationTaxonomies(networkId);
			}else{
				List<StationRole> stationRoles = stationRolesRepository.findByPersonIdAndNetworkId(personLogged.id, network.id);
				List<Integer> stationsIds = new ArrayList<>(stationRoles.size());
				stationsIds.addAll(stationRoles.stream()
						.filter(stationRole -> stationRole.admin)
						.map(stationRole -> stationRole.station.id)
						.collect(Collectors.toList()));
				if(!stationsIds.isEmpty()){
					taxonomies = taxonomyRepository.findByStationsIds(stationsIds);
				}
			}
		}
		return Response.ok().entity(objectMapper.writeValueAsString(taxonomies)).build();
	}

	@Path("/allCategories")
	@GET
	public List<Taxonomy> getCategories(@Context HttpServletRequest request, @QueryParam("stationId") Integer stationId) throws IOException {
		Taxonomy category;

		Station station = stationRepository.findOne(stationId);
		StationPerspective sp = stationPerspectiveRepository.findOne(station.defaultPerspectiveId);
		category = taxonomyRepository.findOne(sp.taxonomy.id);

		List<Term> categoryTerms = new ArrayList<>();
		for(Term term: category.terms){
			categoryTerms.add(term);
			term.cells = null; term.children = null; term.parent = null; term.posts = null; term.rows = null; term.termPerspectives = null; term.taxonomy = null;
		}

		Taxonomy scategory = null;
		List<Taxonomy> staxonomies = taxonomyRepository.findStationTaxonomy(stationId);
		if(staxonomies != null && staxonomies.size() > 0){
			scategory = staxonomies.get(0);
		}

		List<Term> scategoryTerms = new ArrayList<>();
		for(Term term: scategory.terms){
			scategoryTerms.add(term);
			term.cells = null; term.children = null; term.parent = null; term.posts = null; term.rows = null; term.termPerspectives = null; term.taxonomy = null;
		}

		ArrayList<Taxonomy> allTax = new ArrayList<>();

		category.owningNetwork = null; category.owningStation = null; category.networks = null;
		scategory.owningNetwork = null; scategory.owningStation = null; scategory.networks = null;

		category.terms = new HashSet<>(categoryTerms);
		scategory.terms = new HashSet<>(scategoryTerms);

		allTax.add(category);
		allTax.add(scategory);

		return  allTax;
	}
}