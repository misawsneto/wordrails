package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.CommentRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.StationRepository;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ESStartupIndexerService {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	PostRepository postRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	ElasticSearchService elasticSearchService;
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

			elasticSearchService.deleteIndex(index);
			elasticSearchService.createIndex(index);

			List<Identifiable> stations = new ArrayList(stationRepository.findAll());
			List<Identifiable> people = new ArrayList(personRepository.findAll());
			List<Identifiable> posts = Lists.newArrayList(postRepository.findPostWithTerms());
			List<Identifiable> comments = Lists.newArrayList(commentRepository.findAll());

			elasticSearchService.mapThenSave(stations, ESStation.class);
			elasticSearchService.mapThenSave(posts, ESPost.class);
			elasticSearchService.mapThenSave(people, ESPerson.class);
			elasticSearchService.mapThenSave(comments, ESComment.class);
		} else {
			log.info("Elasticsearch indexing is disabled");
		}
	}
}
