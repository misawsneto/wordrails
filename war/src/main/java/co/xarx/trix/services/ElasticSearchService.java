package co.xarx.trix.services;

import co.xarx.trix.domain.MultiTenantEntity;
import co.xarx.trix.elasticsearch.ESBookmarkRepository;
import co.xarx.trix.elasticsearch.ESPersonRepository;
import co.xarx.trix.elasticsearch.ESPostRepository;
import co.xarx.trix.elasticsearch.ESStationRepository;
import co.xarx.trix.elasticsearch.domain.ESBookmark;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.domain.ESStation;
import co.xarx.trix.persistence.*;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ElasticSearchService {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Value("${elasticsearch.index}")
	private String index;


	@Autowired
	protected Client client;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PostRepository postRepository;
	@Autowired
	PostScheduledRepository postScheduledRepository;
	@Autowired
	PostTrashRepository postTrashRepository;
	@Autowired
	PostDraftRepository postDraftRepository;
	@Autowired
	ESPostRepository esPostRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	ESStationRepository esStationRepository;
	@Autowired
	BookmarkRepository bookmarkRepository;
	@Autowired
	ESBookmarkRepository esBookmarkRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ESPersonRepository esPersonRepository;
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

//	@PostConstruct
	public void init() {
		log.info("Start indexing of elasticsearch entities with " + this.toString());

		elasticsearchTemplate.deleteIndex(index);

		List<MultiTenantEntity> stations = Lists.newArrayList(stationRepository.findAll());
		List<MultiTenantEntity> people = Lists.newArrayList(personRepository.findAll());
		List<MultiTenantEntity> bookmarks = Lists.newArrayList(bookmarkRepository.findAll());
		List<MultiTenantEntity> posts = Lists.newArrayList(postRepository.findAll());
		posts.addAll(postScheduledRepository.findAll());
		posts.addAll(postTrashRepository.findAll());
		posts.addAll(postDraftRepository.findAll());

		mapThenSave(stations, ESStation.class, esStationRepository);
		mapThenSave(posts, ESPost.class, esPostRepository);
		mapThenSave(people, ESPerson.class, esPersonRepository);
		mapThenSave(bookmarks, ESBookmark.class, esBookmarkRepository);
	}

	public <D extends MultiTenantEntity> void mapThenSave(List<MultiTenantEntity> itens, Class<D> mapTo, CrudRepository esRepository) {
		int errorCount = 0;
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
					" entities of type " + itens.get(0).getClass().getSimpleName() + " threw mapping error");
			if (entities.size() > 0) log.info("indexing " + entities.size() + " elements of type " + itens.get(0).getClass().getSimpleName());
		}
		entities.forEach(esRepository::save);
	}

	public Client getClient(){
		return client;
	}

	public IndexResponse index(String doc, String id, String index, String type){
		return client == null ? null : client.prepareIndex(index, type, id).setSource(doc).execute().actionGet();
	}

	public UpdateResponse update(String doc, String id, String index, String type){
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(index);
		updateRequest.type(type);
		updateRequest.id(id);
		updateRequest.doc(doc);
		try {
			return client.update(updateRequest).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(String id, String index, String type){
		if(client !=null)
			client.prepareDeleteByQuery(index)
					.setQuery(QueryBuilders.idsQuery(type).addIds(id))
					.execute();
	}
}
