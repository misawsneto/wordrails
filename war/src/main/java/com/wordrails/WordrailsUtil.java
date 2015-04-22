package com.wordrails;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.business.Network;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.RowRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.TermPerspectiveRepository;
import com.wordrails.persistence.TermRepository;

@Component
public class WordrailsUtil {

	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationPerspectiveRepository stationPerspectiveRepository;
	private @Autowired TermPerspectiveRepository termPerspectiveRepository;
	private @Autowired RowRepository rowRepository;
	private @Autowired TermRepository termRepository;
	private @PersistenceContext EntityManager manager;

	/**
	 * This method should be used with caution because it accesses the database.
	 */
	public Network getNetworkFromHost(ServletRequest srq){
		String host = ((HttpServletRequest) srq).getHeader("Host");

		List<Network> networks = null;

		if(host.contains(".xarx.co")){
			String subdomain = host.split(".xarx.co")[0];
			networks = networkRepository.findBySubdomain(subdomain);
		}else if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			networks = networkRepository.findAll();
		}else{
			networks = networkRepository.findByDomain(host);
		}

		Network network = (networks != null && networks.size() > 0) ? networks.get(0) : networkRepository.findOne(1);
		return network;
	}

	@Transactional
	public void incrementReadsCount(Integer id) {
		manager.createNativeQuery("UPDATE Post post SET post.readsCount = post.readsCount + 1 WHERE post.id = :postId").setParameter("postId", id).executeUpdate();
	}
	
	@Transactional
	public void decrementReadsCount(Integer id) {
		manager.createNativeQuery("UPDATE Post post SET post.readsCount = post.readsCount - 1 WHERE post.id = :postId and post.readsCount > 0").setParameter("postId", id).executeUpdate();
	}
	
	@Transactional
	public void incrementFavoritesCount(Integer id) {
		manager.createNativeQuery("UPDATE Post post SET post.favoritesCount = post.favoritesCount + 1 WHERE post.id = :postId").setParameter("postId", id).executeUpdate();
	}
	
	@Transactional
	public void decrementFavoritesCount(Integer id) {
		manager.createNativeQuery("UPDATE Post post SET post.favoritesCount = post.favoritesCount - 1 WHERE post.id = :postId and post.favoritesCount > 0").setParameter("postId", id).executeUpdate();
	}

}
