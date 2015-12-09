package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.eventhandler.ImageEventHandler;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.script.ImageScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
	
	@Autowired public ImageEventHandler imageEventHandler;
	@Autowired public FileRepository fileRepository;
	@Autowired public ImageRepository imageRepository;
	@Autowired public ImageScript imageScript;

	@Async
	public void mergeRepeatedImages(Integer networkId){
		TenantContextHolder.setCurrentTenantId(networkId);
		imageScript.mergeRepeatedImages();
	}


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
