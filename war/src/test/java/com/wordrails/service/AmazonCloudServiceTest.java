package com.wordrails.service;

import com.wordrails.services.AmazonCloudService;
import com.wordrails.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;

@Component
@TransactionConfiguration
@Transactional
public class AmazonCloudServiceTest extends AbstractTest {

	@Autowired
	private AmazonCloudService amazonCloudService;

	@Test
	public void testUpload() throws Exception {
		File file = new File("mat.png");
		FileInputStream is = new FileInputStream(file);
		amazonCloudService.uploadPublicImage(is, file.length(), "demo", "porrass", "medium", "png");
	}

}