package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.TermView;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.converter.TermConverter;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import co.xarx.trix.domain.TermPerspective;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.TermPerspectiveRepository;
import co.xarx.trix.persistence.TermRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.TermsApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.*;

@Component
@NoArgsConstructor
public class TermsResource extends AbstractResource implements TermsApi {

	@Autowired
	private TermRepository termRepository;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private TermPerspectiveRepository termPerspectiveRepository;
	@Autowired
	private TermConverter termConverter;
	@Autowired
	@Qualifier("simpleMapper")
	private ObjectMapper simpleMapper;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private PostRepository postRepository;

	@Override
	public void getTerms() throws IOException {
		forward();
	}

	@Override
	public void postTerm() throws IOException {
		forward();
	}

	@Override
	public void putTerm() throws IOException {
		forward();
	}

	@Override
	public void deleteTerm() throws IOException {
		forward();
	}

	@Override
	public Response getTermTree(Integer taxonomyId, Integer perspectiveId) throws IOException {
		List<Term> allTerms;
		if(perspectiveId != null){
			allTerms = termRepository.findByPerspectiveId(perspectiveId);
		}else{
			allTerms = termRepository.findByTaxonomyId(taxonomyId);
		}

		List<Term> roots =  createTermTree(allTerms);

		String json = simpleMapper.writeValueAsString(roots);
		return Response.status(Status.OK).entity(json).build();
	}

	private List<Term> createTermTree(List<Term> allTerms) {
		List<Term> roots = getRootTerms(allTerms);
		for (Term term : roots) {
			getChilds(term, allTerms);
		}
		return roots;
	}

	private List<Term> getRootTerms(List<Term> allTerms) {
		List<Term> roots = new ArrayList<Term>();
		for (Term term : allTerms) {
			if (term.parent == null) {
				roots.add(term);
			}
		}
		Collections.sort(roots);
		return roots;
	}

	private Set<Term> getChilds(Term parent, List<Term> allTerms) {
		cleanTerm(parent);
		parent.children = new TreeSet<Term>();
		for (Term term : allTerms) {
			if (term.parent != null && parent.id.equals(term.parent.id)) {
				parent.children.add(term);
				term.parent = null;
				term.termPerspectives = null;
			}
		}

		for (Term term : parent.children) {
			getChilds(term, allTerms);
		}

		return parent.children;
	}

	private void cleanTerm(Term term) {
		term.termPerspectives = null;
		term.taxonomy = null;
	}

	@Override
	public Response getTermImage(Integer termId, Integer perspectiveId) throws IOException {
		TermPerspective tp = termPerspectiveRepository.findPerspectiveAndTerm(perspectiveId, termId);
		String hash = ""; // termRepository.findValidHash(perspectiveId, termId);

		if(tp != null && tp.defaultImageHash != null)
			hash = tp.defaultImageHash;
		else {
			Pageable page = new PageRequest(0,1, Sort.Direction.DESC, "date");
			List<Post> posts = postRepository.findByFeaturedImageByTermId(termId, page);
			if(posts!=null && posts.size()>0)
				hash = posts.get(0).getImageLargeHash();
		}

		if (hash != null && !hash.isEmpty()) {
			response.sendRedirect(amazonCloudService.getPublicImageURL(hash));
			return Response.ok().build();
		}

		return Response.status(Status.NO_CONTENT).build();
	}

	@Override
	public ContentResponse<List<TermView>> getAllTerms(Integer taxonomyId, Integer perspectiveId) throws IOException {
		List<Term> allTerms;
		if(perspectiveId != null){
			allTerms = termRepository.findByPerspectiveId(perspectiveId);
		}else{
			allTerms = termRepository.findByTaxonomyId(taxonomyId);
		}

		ContentResponse<List<TermView>> response = new ContentResponse<>();
		response.content = termConverter.convertToViews(allTerms);
		return response;
	}

	@Override
	public ContentResponse<List<PostView>> findPostsByTagAndStationId(String tagName, Integer stationId,
																	  int page, int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));

		List<Post> posts = termRepository.findPostsByTagAndStationId(tagName, stationId, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@Override
	public ContentResponse<List<PostView>> findPostsByTerm(Integer termId, int page, int size, String sort) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));

		List<Post> posts = termRepository.findPostsByTerm(termId, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

}
