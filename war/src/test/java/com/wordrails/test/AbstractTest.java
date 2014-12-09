package com.wordrails.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:applicationContext.xml",
	"classpath:applicationContext-integration.xml",
	"classpath:applicationContext-security.xml",
	"classpath:applicationContext-test.xml"
})
public abstract class AbstractTest {
	protected @Autowired ApplicationContext context;
}