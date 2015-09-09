package com.wordrails.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.persistence.StationRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:applicationContext.xml",
	"classpath:applicationContext-quartz.xml",
	"classpath:applicationContext-integration.xml",
	"classpath:applicationContext-security.xml",
})
public abstract class AbstractTest extends AbstractTransactionalJUnit4SpringContextTests {
	protected @Autowired ApplicationContext context;
	private @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	//private @Autowired StationRepository stationRepository;
	
	@Test
	public  void doTest() {
//		Station station = stationRepository.findOne(1);
//		System.out.println(station.stationPerspectives.toString());
	}
}