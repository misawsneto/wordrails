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
public class WordrailsService {

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

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			networks = networkRepository.findAll();
		}else{
			String[] names = host.split("\\.");
			String topDomain = names[names.length - 2] + "." + names[names.length - 1];
			String subdomain = !topDomain.equals(host) ? host.split("." + topDomain)[0] : null;
			if(subdomain != null && !subdomain.isEmpty())
				networks = networkRepository.findBySubdomain(subdomain);
		}
		
		if(networks == null || networks.size() == 0){
			networks = networkRepository.findByDomain(host);
		}

		Network network = (networks != null && networks.size() > 0) ? networks.get(0) : networkRepository.findOne(1);
		return network;
	}

}
