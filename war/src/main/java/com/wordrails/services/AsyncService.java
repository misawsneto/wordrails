package com.wordrails.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.wordrails.eventhandler.ImageEventHandler;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;

@Service
public class AsyncService {
	
	@Autowired public ImageEventHandler imageEventHandler;
	@Autowired public FileRepository fileRepository;
	@Autowired public ImageRepository imageRepository;

	@Async
	public void test() {
		try {
			Thread.sleep(600);
			System.out.println("sleeped...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
