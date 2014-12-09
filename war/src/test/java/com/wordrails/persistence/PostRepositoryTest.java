package com.wordrails.persistence;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.wordrails.test.AbstractTest;

public class PostRepositoryTest extends AbstractTest {
	private @Autowired PostRepository repository;
	
	@Test
	public void test() {
		Sort sort = new Sort(Direction.DESC, "date");
		Pageable pageable = new PageRequest(0, 10, sort);
		repository.findPostsFromOrPromotedToStation(1, pageable);
		repository.findAll(sort);
	}
	
	@Test
	public void findByStationIdAndTitleTest() {
		Sort sort = new Sort(Direction.DESC, "date");
		Pageable pageable = new PageRequest(0, 10, sort);
		//repository.findByStationIdAndTitle(1, "Diogo Peixoto", pageable);
		//repository.findByStationIdAndTermsId(1, 2, pageable);
	}
}