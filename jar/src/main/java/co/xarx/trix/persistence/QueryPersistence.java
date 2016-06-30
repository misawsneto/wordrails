package co.xarx.trix.persistence;

import co.xarx.trix.domain.Post;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Component
public class QueryPersistence {
	private @PersistenceContext EntityManager manager;

	@Transactional
	public void updateCommentsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post p set commentsCount = (select count(*) FROM comment c WHERE c.post_id = :postId1) WHERE p.id = :postId2").setParameter("postId1", postId).setParameter("postId2", postId).executeUpdate();
	}

	@Transactional
	public void deletePostReadsInPosts(List<Integer> ids) {
		manager.createQuery("delete from PostRead postRead where postRead.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}

	@Transactional
	public void deleteCellsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Cell cell where cell.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteCommentsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Comment comment where comment.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}

	@Transactional
	public List<Object[]> getPersonPublicationsCount(Integer personId) {
		return manager.createNativeQuery("select (select count(*) from Post po where po.author_id = p.id and po.state = 'PUBLISHED')," +
										"(select count(*) from Post po where po.author_id = p.id and po.state = 'DRAFT')," +
										"(select count(*) from Post po where po.author_id = p.id and po.state = 'SCHEDULED')" +
				"from Person p where p.id = :personId").setParameter("personId", personId).getResultList();
	}

	@Transactional
	public void setNoAuthor(Integer personId) {
		manager.createNativeQuery("UPDATE Post post SET post.author_id = 1, post.state = 'NOAUTHOR' WHERE post.author_id = :personId").setParameter("personId", personId).executeUpdate();
	}

	@Transactional
	public List<Object[]> getStationsPublicationsCount(List<Integer> stationIds) {
		return manager.createNativeQuery("select (select count(*) from Post po where po.station_id = s.id and po.state = 'PUBLISHED')," +
				"(select count(*) from Post po where po.station_id = s.id and po.state = 'DRAFT')," +
				"(select count(*) from Post po where po.station_id = s.id and po.state = 'SCHEDULED')" +
				"from Station s where s.id in (:stationIds)").setParameter("stationIds", stationIds).getResultList();
	}

	@Transactional
	public void updateDefaultPerspective(Integer id, Integer perspectiveId) {
		manager.createQuery("update Station set defaultPerspectiveId = :defaultPerspectiveId where id = :id").setParameter("defaultPerspectiveId", perspectiveId).setParameter("id", id).executeUpdate();
	}

	@Transactional
	public void deleteFeaturedRow(Integer perspectiveId, Integer notId) {
		manager.createQuery("delete from Row where type = 'F' and featuringPerspective.id = :perspectiveId and id <> :notId")
				.setParameter("perspectiveId", perspectiveId).setParameter("notId", notId).executeUpdate();
	}

	@Transactional
	public void deleteSplashedRow(Integer perspectiveId, Integer notId) {
		manager.createQuery("delete from Row where type = 'S' and splashedPerspective.id = :perspectiveId and id <> :notId")
				.setParameter("perspectiveId", perspectiveId).setParameter("notId", notId).executeUpdate();
	}

	public List<Post> findPostsByTag(Set<String> tags, int page, int size) {
		return manager.createQuery("select distinct p from Post p join p.tags tgs where tgs in :tags", Post.class)
				.setParameter("tags", tags)
				.setMaxResults(size)
				.setFirstResult(page * size)
				.getResultList();
	}

	public List<Post> findPostsByTagAndStationId(Set<String> tags, Integer stationId, int page, int size) {
		return manager.createQuery("select distinct p from Post p join p.tags tgs where tgs in (:tags) and p.station.id = :stationId", Post.class)
				.setParameter("tags", tags)
				.setParameter("stationId", stationId)
				.setMaxResults(size)
				.setFirstResult(page * size)
				.getResultList();
	}

	@Transactional
	public void deleteAuthoritiesByStation(Integer id) {
		manager.createNativeQuery("DELETE from authorities where station_id = (:id)").setParameter("id", id).executeUpdate();
	}

	@Transactional
	public void updateBookmarksCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post p set p.bookmarksCount = (select count(*) FROM person_bookmark pb WHERE pb.post_id" +
				" = :postId1) WHERE p.id = :postId2").setParameter("postId1", postId).setParameter("postId2", postId).executeUpdate();
	}

	@Transactional
	public void updateRecommendsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post p set p.recommendsCount = (select count(*) FROM person_recommend pr WHERE" +
				" pr.post_id = :postId1) WHERE p.id = :postId2").setParameter("postId1", postId).setParameter("postId2", postId).executeUpdate();
	}
}
