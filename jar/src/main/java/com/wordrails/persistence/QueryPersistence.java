package com.wordrails.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
		manager.createNativeQuery("UPDATE Post post SET post.recommendsCount = post.favoritesCount - 1 WHERE post.id = :postId and post.recommendsCount > 0").setParameter("postId", postId).executeUpdate();
	}

}
