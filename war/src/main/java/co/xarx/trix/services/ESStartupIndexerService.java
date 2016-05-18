package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.StationRepository;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.client.Requests.createIndexRequest;

@Service
public class ESStartupIndexerService {

	Logger log = Logger.getLogger(this.getClass().getName());
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PostRepository postRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	Client client;
	@Value("${spring.data.elasticsearch.index}")
	private String index;
	@Value("${trix.elasticsearch.reindex}")
	private boolean indexES;

	@PostConstruct
	public void init() {
		if (indexES) {
			log.info("Start indexing of elasticsearch entities");

			elasticsearchTemplate.deleteIndex(index);

			List<Identifiable> stations = new ArrayList(stationRepository.findAll());
			List<Identifiable> people = new ArrayList(personRepository.findAll());
			List<Identifiable> posts = Lists.newArrayList(postRepository.findPostWithJoins());

			mapThenSave(stations, ESStation.class);
			mapThenSave(posts, ESPost.class);
			mapThenSave(people, ESPerson.class);
		} else {
			log.info("Elasticsearch indexing is disabled");
		}
	}

	public <ES extends ElasticSearchEntity> void mapThenSave(List<Identifiable> itens, Class<ES> mapTo) {
		int errorCount = 0;
		List<ElasticSearchEntity> entities = new ArrayList<>();
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
			if (entities.size() > 0)
				log.info("indexing " + entities.size() + " elements of type " + itens.get(0).getClass().getSimpleName());
		}

		if (!isThereIndex(index)) createIndex();

		entities.forEach(this::saveIndex);
	}

	public <ES extends ElasticSearchEntity> void mapThenSave(Identifiable item, Class<ES> mapTo) {
		mapThenSave(Collections.singletonList(item), mapTo);
	}

	public void saveIndex(ElasticSearchEntity entity) {
//		ElasticSearchEntity entity = modelMapper.map(object, objectClass);
//		elasticsearchTemplate.putMapping(objectClass);
//		elasticsearchTemplate.refresh(objectClass, true);

		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(String.valueOf((entity).getId()));
		indexQuery.setObject(entity);

		elasticsearchTemplate.index(indexQuery);
	}

	private void createIndex() {
		client.admin()
				.indices()
				.create(createIndexRequest(index)).actionGet();
	}

	private boolean isThereIndex(String index) {
		ImmutableOpenMap<String, IndexMetaData> indexMap = client.admin().cluster()
				.prepareState().get().getState().getMetaData().getIndices();

		return indexMap.containsKey(index);
	}
}
