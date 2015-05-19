package com.wordrails.persistence;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wordrails.test.AbstractTest;
import com.wordrails.util.WordrailsUtil;

public class PersonNetworkRegIdRepositoryTest extends AbstractTest {
	private @Autowired PersonNetworkRegIdRepository repository;
	private @Autowired QueryPersistence qp;
	
	@Test
	public void test() {
		List<String> strings = Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12", "13");
		List<List<String>> parts = WordrailsUtil.partition(strings, 1000);
		for (List<String> list : parts) {
			System.out.println(list);
		}
//		Sort sort = new Sort(Direction.DESC, "date");
//		Pageable pageable = new PageRequest(0, 10, sort);
	}	
}