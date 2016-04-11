package co.xarx.trix.services;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.domain.MultiTenantEntity;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.QPost;
import co.xarx.trix.persistence.ESRepository;
import co.xarx.trix.domain.ESPerson;
import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.ESStation;
import co.xarx.trix.persistence.ESPersonRepository;
import co.xarx.trix.persistence.ESPostRepository;
import co.xarx.trix.persistence.ESStationRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.StationRepository;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ESStartupIndexerService {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Value("${spring.data.elasticsearch.index}")
	private String index;
	@Value("${trix.elasticsearch.reindex}")
	private boolean indexES;


	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PostRepository postRepository;
	@Autowired
	ESPostRepository esPostRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	ESStationRepository esStationRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ESPersonRepository esPersonRepository;
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	@PostConstruct
	public void init() {
		if(indexES) {
			log.info("Start indexing of elasticsearch entities");

			elasticsearchTemplate.deleteIndex(index);

			List<MultiTenantEntity> stations = new ArrayList(stationRepository.findAll());
			List<MultiTenantEntity> people = new ArrayList(personRepository.findAll());
			List<MultiTenantEntity> posts = Lists.newArrayList(postRepository.findAll(QPost.post.state.eq(Post.STATE_PUBLISHED)));

			mapThenSave(stations, ESStation.class, esStationRepository);
			mapThenSave(posts, ESPost.class, esPostRepository);
			mapThenSave(people, ESPerson.class, esPersonRepository);
		} else {
			log.info("Elasticsearch indexing is disabled");
		}
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

	public <T extends ElasticSearchEntity> void saveIndex(Object object, Class<T> objectClass, ESRepository esRepository) {
		ElasticSearchEntity entity = modelMapper.map(object, objectClass);
		esRepository.save(entity);
	}
}
