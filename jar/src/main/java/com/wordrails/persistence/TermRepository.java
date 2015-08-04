package com.wordrails.persistence;

import com.wordrails.business.Post;
import com.wordrails.business.Taxonomy;
import com.wordrails.business.Term;
import com.wordrails.business.Wordpress;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface
		TermRepository extends JpaRepository<Term, Integer>, QueryDslPredicateExecutor<Term> {
	@RestResource(path="findRootsPage")
	List<Term> findRoots(@Param("taxonomyId") Integer taxonomyId, Pageable pageable);
	
	List<Term> findRoots(@Param("taxonomyId") Integer taxonomyId);
	
	@RestResource(exported=false)
	List<Term> findByParent(@Param("term") Term term);
	
	Integer countTerms(@Param("termsIds") List<Integer> termsIds);
	
	@RestResource(path="findTermsByParentId")
	List<Term> findByParentId(@Param("termId") Integer termId, Pageable pageable);

	@RestResource(exported=false)
	Term findTreeByTermId(@Param("termId") Integer termId);

	@RestResource(exported=false)
	Term findByWordpressIdAndTaxonomy(@Param("wordpressId") Integer wordpressId, @Param("taxonomy") Taxonomy taxonomy);

	@RestResource(exported=false)
	Term findByWordpressSlugAndTaxonomy(@Param("wordpressSlug") String wordpressSlug, @Param("taxonomy") Taxonomy taxonomy);

	@RestResource(exported=false)
	List<Term> findTreeByTaxonomyId(@Param("taxonomyId") Integer taxonomyId);

	@RestResource(exported=false)
	List<Term> findByTaxonomy(@Param("taxonomy") Taxonomy taxonomy);

	@RestResource(exported=false)
	List<Term> findTagsByWordpress(@Param("wordpress") Wordpress wordpress);

	@RestResource(exported=false)
	List<Term> findCategoriesByWordpress(@Param("wordpress") Wordpress wordpress);

	@RestResource(exported=false)
	Term findByWordpressId(@Param("wordpressId") Integer wordpressId);
	
	@RestResource(exported=false)
	Term findTermAuthorTaxonomy(@Param("termName") String name, @Param("taxonomy") Taxonomy taxonomy);
	
	@RestResource(exported=false)
	@Modifying
	@Query(value="UPDATE Term term SET term.name = :newName WHERE term.name = :oldName")
	void updateTermsNamesAuthorTaxonomies(@Param("newName") String newName, @Param("oldName") String oldName);
	
	@Query(value="SELECT post.terms FROM Post post where post.id = :postId")
	List<Term> findTermsByPostId(@Param("postId") Integer postId);
	
	@Query(value="SELECT post.terms FROM Post post where post.slug = :slug")
	List<Term> findTermsByPostSlug(@Param("slug") String slug);
	
	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM post_term WHERE terms_id = ?")
	void deletePostsTerms(Integer termId);

	@Query(value="SELECT taxonomy.terms FROM StationPerspective sp join sp.taxonomy taxonomy where sp.id = :perspectiveId")
	List<Term> findByPerspectiveId(@Param("perspectiveId") Integer perspectiveId);
	
	@Query(value="SELECT taxonomy.terms FROM Taxonomy taxonomy where taxonomy.id = :taxonomyId")
	List<Term> findByTaxonomyId(@Param("taxonomyId") Integer perspectiveId);

	@Query(value="SELECT post FROM Post post left join post.terms term where post.state = 'SCHEDULED' and post.stationId = :stationId and term.name = :tagName")
	List<Post> findPostsByTagAndStationId(@Param("tagName") String tagName, @Param("stationId")Integer stationId, Pageable pageable);
}