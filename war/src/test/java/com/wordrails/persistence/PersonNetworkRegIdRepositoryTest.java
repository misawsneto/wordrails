package com.wordrails.persistence;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.wordrails.business.PersonNetworkRegId;
import com.wordrails.test.AbstractTest;

public class PersonNetworkRegIdRepositoryTest extends AbstractTest {
	private @Autowired PersonNetworkRegIdRepository repository;
	private @Autowired QueryPersistence qp;
	
	@Test
	public void test() {
//		Sort sort = new Sort(Direction.DESC, "date");
//		Pageable pageable = new PageRequest(0, 10, sort);
		System.out.println(repository.findRegIdByStationId(2));
	}	
}