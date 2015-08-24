package com.wordrails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AmazonCloudService {

	@Autowired
	private ResourceLoader resourceLoader;

	public void loadFile(String bucket, String fileName) throws IOException {
		Resource resource = this.resourceLoader.getResource("s3://" + bucket + "/" + fileName);

		InputStream inputStream = resource.getInputStream();
	}
}
