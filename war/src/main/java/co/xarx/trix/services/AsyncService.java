package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.AbstractConverter;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.elasticsearch.ESRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.script.ImageScript;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsyncService {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	public ImageScript imageScript;
	@Autowired
	public GCMService gcmService;
	@Autowired
	public APNService apnService;
	@Autowired
	public PostService postService;
	@Autowired
	public PostRepository postRepository;
	@Autowired
	public PostConverter postConverter;
	@Autowired
	public AndroidBuilderService androidBuilderService;

	private List<ElasticSearchEntity> getESEntities(CrudRepository repository, AbstractConverter converter) {
		List<ElasticSearchEntity> posts = new ArrayList<>();
		Iterable<Object> all = repository.findAll();
		for (Object post : all) {
			posts.add((ElasticSearchEntity) converter.convertToView(post));
		}

		return posts;
	}

	@Async
	public void index(Integer networkId, String tenantId, CrudRepository repository,
	                                                  AbstractConverter converter, ESRepository esRepository) {
		TenantContextHolder.setCurrentNetworkId(networkId);
		TenantContextHolder.setCurrentTenantId(tenantId);
		List<ElasticSearchEntity> esEntities = getESEntities(repository, converter);
		log.info("indexing " + esEntities.size() + " elements, tenant " + tenantId);
		esEntities.forEach((entity) -> {
			esRepository.save(entity);
			log.debug("index post " + entity.getId() + " on " + entity.getTenantId());
		});
	}

	@Async
	public void imagePictureScript(Integer networkId) {
		TenantContextHolder.setCurrentNetworkId(networkId);
		imageScript.addPicturesToImages();
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
