package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.script.ImageScript;
import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
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

	@Async
	public <D extends MultiTenantEntity> void mapThenSave(QueryDslPredicateExecutor repository, Predicate predicate, ModelMapper modelMapper
			, Class<D> mapTo, CrudRepository esRepository) {
		int errorCount = 0;
		List<MultiTenantEntity> itens = Lists.newArrayList(repository.findAll(predicate));
		List<MultiTenantEntity> entities = new ArrayList<>();
		for (Object item : itens) {
			try {
				entities.add(modelMapper.map(item, mapTo));
			} catch (Exception e) {
				errorCount++;
			}
		}

		if (itens.size() > 0) {
			if (errorCount > 0) log.info("mapping of " + errorCount + "/" + itens.size() +
					" entities on tenant " + itens.get(0).getTenantId() + " threw mapping error");
			if (entities.size() > 0) log.info("indexing " + entities.size() + " elements on tenant " + itens.get(0).getTenantId());
		}
		entities.forEach(esRepository::save);
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
