package com.wordrails.persistence;

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
}
