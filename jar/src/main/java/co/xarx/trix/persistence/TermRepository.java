package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.domain.Term;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource(exported = true)
public interface TermRepository extends DatabaseRepository<Term, Integer> {

	@Override
	@SdkExclude
	@RestResource(exported = true)
	void delete(Term term);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Term save(Term term);

	@RestResource(path="findRootsPage")
	@Query("select term from Term term where term.parent is null and term.taxonomy.id = :taxonomyId")
	List<Term> findRoots(@Param("taxonomyId") Integer taxonomyId, Pageable pageable);

	@Query("select term from Term term where term.parent is null and term.taxonomy.id = :taxonomyId")
	List<Term> findRoots(@Param("taxonomyId") Integer taxonomyId);

	@RestResource(exported=false)
	List<Term> findByParent(@Param("term") Term term);

	@Query("select count(*) from Term term where term.id IN (:termsIds)")
	Integer countTerms(@Param("termsIds") List<Integer> termsIds);

	@RestResource(path="findTermsByParentId")
	List<Term> findByParentId(@Param("termId") Integer termId, Pageable pageable);

	@Query("select t from Term t left join fetch t.children where t.id = :termId")
	@RestResource(exported=false)
	Term findTreeByTermId(@Param("termId") Integer termId);

	@RestResource(exported=false)
	List<Term> findByTaxonomy(@Param("taxonomy") Taxonomy taxonomy);

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

    @RestResource(exported = false)
    @Query(value="SELECT term.id FROM Term term where term.taxonomyId = :taxonomyId")
    List<Integer> findTermIdsByTaxonomyId(@Param("taxonomyId") Integer taxonomyId);

	@Query(value="SELECT taxonomy.terms FROM Taxonomy taxonomy where taxonomy.id = :taxonomyId")
	List<Term> findByTaxonomyId(@Param("taxonomyId") Integer taxonomyId);

	@RestResource(exported = false)
	@Query(value="SELECT post FROM Post post left join post.terms term where post.state = 'PUBLISHED' and post.stationId = :stationId and term.name = :tagName")
	List<Post> findPostsByTagAndStationId(@Param("tagName") String tagName, @Param("stationId")Integer stationId, Pageable pageable);

	@RestResource(exported = false)
	@Query(value="SELECT post FROM Post post join post.terms term where post.state = 'PUBLISHED' and term.id = :termId")
	List<Post> findPostsByTerm(@Param("termId") Integer termId, Pageable pageable);
}