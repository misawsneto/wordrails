package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.TaxonomiesApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class TaxonomiesResource extends AbstractResource implements TaxonomiesApi {

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

	@Override
	public void findNetworkCategories() throws IOException {
		forward();
	}

//	@Override
//	public Response getTaxonomiesToEdit(Integer networkId) throws IOException {
//		List<Taxonomy> taxonomies = null;
//
//		Network network = networkRepository.findOne(networkId);
//		if(network != null){
//			Person personLogged = authProvider.getLoggedPerson();
//			if(personLogged.networkAdmin){
//				taxonomies = taxonomyRepository.findNetworkOrStationTaxonomies(networkId);
//			}else{
//				List<StationRole> stationRoles = stationRolesRepository.findByPersonIdAndNetworkId(personLogged.id, network.id);
//				List<Integer> stationsIds = new ArrayList<>(stationRoles.size());
//				stationsIds.addAll(stationRoles.stream()
//						.filter(stationRole -> stationRole.admin)
//						.map(stationRole -> stationRole.station.id)
//						.collect(Collectors.toList()));
//				if(!stationsIds.isEmpty()){
//					taxonomies = taxonomyRepository.findByStationsIds(stationsIds);
//				}
//			}
//		}
//		return Response.ok().entity(objectMapper.writeValueAsString(taxonomies)).build();
//	}

	@Override
	public List<Taxonomy> getCategories(Integer stationId) throws IOException {
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