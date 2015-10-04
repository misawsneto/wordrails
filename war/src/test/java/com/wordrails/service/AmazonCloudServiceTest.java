package com.wordrails.service;

import com.wordrails.services.AmazonCloudService;
import com.wordrails.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Component
@TransactionConfiguration
@Transactional
public class AmazonCloudServiceTest extends AbstractTest {

	@Autowired
	private AmazonCloudService amazonCloudService;

	@Test
	public void testUpload() throws Exception {
	}

}