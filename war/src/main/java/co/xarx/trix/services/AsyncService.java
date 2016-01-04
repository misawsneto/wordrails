package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AndroidApp;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsyncService {

	@Autowired
	public GCMService gcmService;
	@Autowired
	public APNService apnService;
	@Autowired
	public PostService postService;
	@Autowired
	public AndroidBuilderService androidBuilderService;

	@Async
	public void run(Runnable runnable) {
		runnable.run();
	}

	@Async
	public void run(Integer networkId, Runnable runnable) {
		TenantContextHolder.setCurrentNetworkId(networkId);
		runnable.run();
	}

	@Async
	public void notifyAndroid(Integer networkId, Integer stationId, Notification notification){
		TenantContextHolder.setCurrentNetworkId(networkId);
		gcmService.sendToStation(stationId, notification);
	}

	@Async
	public void notifyApple(Integer networkId, Integer stationId, Notification notification){
		TenantContextHolder.setCurrentNetworkId(networkId);
		apnService.sendToStation(networkId, stationId, notification);
	}

	@Async
	public void buildAndroidApp(Integer networkId, String configPath, AndroidApp androidApp) throws Exception {
		TenantContextHolder.setCurrentNetworkId(networkId);
		androidBuilderService.run(configPath, androidApp);
	}

	@Async
	@Transactional
	public void countPostRead(Integer networkId, Post post, Person person, String sessionId) {
		TenantContextHolder.setCurrentNetworkId(networkId);
		postService.countPostRead(post, person, sessionId);
	}
}
