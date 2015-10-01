package com.wordrails.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QueryPersistence {
	private @PersistenceContext EntityManager manager;
	
	@Transactional
	public void incrementReadsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.readsCount = post.readsCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementReadsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.readsCount = post.readsCount - 1 WHERE post.id = :postId and post.readsCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void incrementBookmarksCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.bookmarksCount = post.bookmarksCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementBookmarksCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.bookmarksCount = post.bookmarksCount - 1 WHERE post.id = :postId and post.bookmarksCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void incrementFavoritesCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.favoritesCount = post.favoritesCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementFavoritesCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.favoritesCount = post.favoritesCount - 1 WHERE post.id = :postId and post.favoritesCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void incrementRecommendsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.recommendsCount = post.recommendsCount + 1 WHERE post.id = :postId").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void decrementRecommendsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post post SET post.recommendsCount = post.recommendsCount - 1 WHERE post.id = :postId and post.recommendsCount > 0").setParameter("postId", postId).executeUpdate();
	}
	
	@Transactional
	public void deleteBookmark(Integer postId, Integer personId){
		manager.createNativeQuery("DELETE FROM Bookmark WHERE post_id = :postId AND person_id = :personId").setParameter("postId", postId).setParameter("personId", personId).executeUpdate();
	}

	@Transactional
	public void deleteRecommend(Integer postId, Integer personId) {
		manager.createNativeQuery("DELETE FROM Recommend WHERE post_id = :postId AND person_id = :personId").setParameter("postId", postId).setParameter("personId", personId).executeUpdate();
	}

	@Transactional
	public void changePostState(Integer postId, String state) {
		manager.createNativeQuery("UPDATE post SET state=:state where id = :postId").setParameter("postId", postId).setParameter("state", state).executeUpdate();
	}
	
	@Async
	@Transactional
	public void updateCommentsCount(Integer postId) {
		manager.createNativeQuery("UPDATE Post set commentsCount = (select count(*) FROM comment WHERE post_id = :postId) WHERE id = postId;").setParameter("postId", postId).executeUpdate();
	}

	@Transactional
	public void deletePostReadsInPosts(List<Integer> ids) {
		manager.createQuery("delete from PostRead postRead where postRead.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteNotificationsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Notification notification where notification.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}

	@Transactional
	public void deleteBookmarksInPosts(List<Integer> ids) {
		manager.createQuery("delete from Bookmark bookmark where bookmark.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
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
	public void deleteImagesInPosts(List<Integer> ids) {
		manager.createQuery("delete from Image image where image.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deletePromotionsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Promotion promotion where promotion.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}
	
	@Transactional
	public void deleteRecommendsInPosts(List<Integer> ids) {
		manager.createQuery("delete from Recommend recommend where recommend.post.id in (:ids)").setParameter("ids", ids).executeUpdate();
	}

	@Transactional
	public void updateMainStation(Integer id, boolean main) {
		manager.createNativeQuery("update Station station set main = :main where station.id = :id").setParameter("id", id).setParameter("main", main).executeUpdate();		
	}

	@Transactional
	public List<Object[]> getPersonPublicationsCount(Integer personId) {
		return manager.createNativeQuery("select (select count(*) from Post po where po.author_id = p.id and po.state = 'PUBLISHED')," +
										"(select count(*) from Post po where po.author_id = p.id and po.state = 'DRAFT')," +
										"(select count(*) from Post po where po.author_id = p.id and po.state = 'SCHEDULED')" +
				"from Person p where p.id = :personId").setParameter("personId", personId).getResultList();
	}
	public void updateLastLogin(String username) {
		// TODO Auto-generated method stub
		manager.createQuery("update Person person set person.lastLogin = :date where person.username = :username").setParameter("username", username).setParameter("date", new Date()).executeUpdate();
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
}
