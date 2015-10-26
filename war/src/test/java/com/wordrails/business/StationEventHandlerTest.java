package com.wordrails.business;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.test.AbstractTest;

public class StationEventHandlerTest extends AbstractTest {

	@Autowired private StationEventHandler handler;
	@Autowired private NetworkEventHandler networkHandler;

	@Autowired private StationRepository stationRepository; 
	@Autowired private NetworkRepository networkRepository; 
	@Autowired private StationPerspectiveRepository stationPerspectiveRepository; 

	private final String STATION_NAME = "wordRAILS";
	private final String NETWORK_NAME = "XARX";

	@Test
	public void handleBeforeCreateTestFail() throws Exception {
//		Station station = new Station();
//		station.name = STATION_NAME;
//
//		boolean thrownException = false;
//		try{
//			handler.handleBeforeCreate(station);
//			stationRepository.save(station);
//		}catch(ConstraintViolationException e){
//			thrownException = true;
//		}
//		Assert.isTrue(thrownException);
	}
	
	@Test
	public void handleBeforeCreateTestPass() throws Exception {
		
		Network network = new Network();
		network.name = NETWORK_NAME;
		networkHandler.handleBeforeCreate(network);
		networkRepository.save(network);
		
		Station station = new Station();
		station.name = STATION_NAME;
//		Set<Network> networks = new HashSet<Network>();
//		networks.add(network);
		station.network = network;
		handler.handleBeforeCreate(station);
		stationRepository.save(station);
		
		Assert.isTrue(station.id != null);
		Assert.isTrue(station.stationPerspectives != null && station.stationPerspectives.size() == 1);
	}

	@Test
	public void handleBeforeCreateTestPass2() throws Exception {
		Station station = new Station();
		station.name = STATION_NAME;

		handler.handleBeforeCreate(station);
		stationRepository.save(station);

		Assert.notNull(station.id);
		Assert.isTrue(station.stationPerspectives.size() == 1);

		List<StationPerspective> stationsPerspectives = stationPerspectiveRepository.findByStationId(station.id);
		Assert.isTrue(stationsPerspectives.size() == 1);
	}
}