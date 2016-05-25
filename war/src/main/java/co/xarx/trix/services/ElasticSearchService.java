package co.xarx.trix.services;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.domain.Identifiable;
import co.xarx.trix.elasticsearch.mapper.ESMapper;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.client.Requests.createIndexRequest;

@Service
public class ElasticSearchService {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	Client client;
	@Value("${spring.data.elasticsearch.index}")
	private String index;

	private static final int SPLIT_FACTOR = 5;

	private <ES extends ElasticSearchEntity> List<ElasticSearchEntity> mapAndCount(List<Identifiable> itens, Class<ES> mapTo) {
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
		return entities;
	}

	public <ES extends ElasticSearchEntity, T> void mapThenSave(List<T> itens, ESMapper<ES, T> mapper) {
		int errorCount = 0;
		List<ES> entities = new ArrayList<>();
		for (T item : itens) {
			try {
				entities.add(mapper.asDto(item));
			} catch (Exception e) {
				errorCount++;
			}
		}

		printErrorMessages(itens, errorCount, entities);
		entities.forEach(this::saveEntity);
	}

	public <ES extends ElasticSearchEntity> void mapThenSave(List<Identifiable> itens, Class<ES> mapTo) {
		List<ElasticSearchEntity> entities = mapAndCount(itens, mapTo);

		if (!isThereIndex(index)) createIndex();

		entities.forEach(this::saveIndex);
	}

	public <ES extends ElasticSearchEntity> void mapThenBulkSave(List<Identifiable> itens, Class<ES> mapTo) {
		List<ElasticSearchEntity> entities = mapAndCount(itens, mapTo);

		if (!isThereIndex(index)) createIndex();

		List<List<ElasticSearchEntity>> chunks = splitList(entities);
		for (List<ElasticSearchEntity> eList : chunks) {
//			asyncService.asyncBulkSaveIndex(createIndexQueries(eList));
			bulkSaveIndex(createIndexQueries(eList));
		}
	}

	public List<List<ElasticSearchEntity>> splitList(List<ElasticSearchEntity> entities) {
		return Lists.partition(entities, (entities.size() + SPLIT_FACTOR - 1) / SPLIT_FACTOR);
	}

	public void bulkSaveIndex(List<IndexQuery> queries) {
		elasticsearchTemplate.bulkIndex(queries);
	}

	public List<IndexQuery> createIndexQueries(List<ElasticSearchEntity> entities) {
		List<IndexQuery> iq = new ArrayList<>();
		for (ElasticSearchEntity et : entities) {
			iq.add(createIndexQuery(et));
		}
		return iq;
	}

	public IndexQuery createIndexQuery(ElasticSearchEntity entity) {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(String.valueOf((entity).getId()));
		indexQuery.setObject(entity);

		return indexQuery;
	}

	public <ES extends ElasticSearchEntity> void mapThenSave(Identifiable item, Class<ES> mapTo) {
		mapThenSave(Collections.singletonList(item), mapTo);
	}

	public void saveIndex(ElasticSearchEntity entity) {
//		ElasticSearchEntity entity = modelMapper.map(object, objectClass);
//		elasticsearchTemplate.putMapping(objectClass);
//		elasticsearchTemplate.refresh(objectClass, true);

		IndexQuery indexQuery = createIndexQuery(entity);
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


	private <ES extends ElasticSearchEntity, T> void printErrorMessages(List<T> itens, int errorCount, List<ES> entities) {
		if (itens.size() > 0) {
			if (errorCount > 0) log.info("mapping of " + errorCount + "/" + itens.size() +
					" entities of type " + itens.get(0).getClass().getSimpleName() + " threw mapping error");
			if (entities.size() > 0)
				log.info("indexing " + entities.size() + " elements of type " + itens.get(0).getClass().getSimpleName());
		}
	}

	public boolean deleteIndex(String index) {
		return elasticsearchTemplate.deleteIndex(index);
	}

	public void createIndex(String index) {
		client.admin()
				.indices()
				.create(createIndexRequest(index)).actionGet();
	}

	public void deleteEntity(String id, Class clazz) {
		elasticsearchTemplate.delete(clazz, id);
	}

	public <ES extends ElasticSearchEntity, T> void mapThenSave(T item, Class<ES> mapTo) {
		mapThenSave(Collections.singletonList(item), mapTo);
	}

	public void saveEntity(ElasticSearchEntity entity) {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(String.valueOf((entity).getId()));
		indexQuery.setObject(entity);

		elasticsearchTemplate.index(indexQuery);
	}


}
