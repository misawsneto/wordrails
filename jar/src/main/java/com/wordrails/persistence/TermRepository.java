package com.wordrails.persistence;

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

public interface TermRepository extends JpaRepository<Term, Integer>, QueryDslPredicateExecutor<Term> {
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
	Term findByWordpressSlug(@Param("wordpressSlug") String wordpressSlug);

	@RestResource(exported=false)
	Term findTreeByTaxonomyId(@Param("taxonomyId") Integer taxonomyId);

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
	
	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM post_term WHERE terms_id = ?")
	void deletePostsTerms(Integer termId);
}