package com.wordrails;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.api.TermPerspectiveView;
import com.wordrails.business.Network;
import com.wordrails.business.Row;
import com.wordrails.business.StationPerspective;
import com.wordrails.business.Term;
import com.wordrails.business.TermPerspective;
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

}
