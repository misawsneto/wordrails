package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.security.PersonPermissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static co.xarx.trix.util.Logger.debug;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class StatisticsService {

	private PostRepository postRepository;
    private PersonRepository personRepository;
    private CommentRepository commentRepository;
    private NetworkRepository networkRepository;
    private StationRepository stationRepository;
    private AnalyticsSearchService analyticsSearchService;

	@Autowired
	public StatisticsService(
            PostRepository postRepository,
            CommentRepository commentRepository,
            PersonRepository personRepository,
            NetworkRepository networkRepository, StationRepository stationRepository,
            AnalyticsSearchService analyticsSearchService) {
		this.postRepository = postRepository;
		this.personRepository = personRepository;
        this.networkRepository = networkRepository;
        this.stationRepository = stationRepository;
		this.analyticsSearchService = analyticsSearchService;
        this.commentRepository = commentRepository;
	}

	public Map getMostPupularNetwork(Integer size){
        Interval interval = getInterval(DateTime.now(), 30);
        return analyticsSearchService.findMostPopular("tenantId", interval, size);
    }

	public Map getMostPorpular(String field, String start, String end, Integer size) throws Exception {
	    Interval interval = getInterval(end, start);
		return analyticsSearchService.findMostPopular(field, interval, size);
	}

	public StatsData getPostStats(String date, Integer postId){
	    Interval interval = getInterval(date, 30);
        Post post = postRepository.findOne(postId);

        return analyticsSearchService.getPostStats(post, interval);
    }

	public StatsData getPostStats(String end, String beginning, Integer postId){
		Interval interval = getInterval(end, beginning);
		Post post = postRepository.findOne(postId);
		return analyticsSearchService.getPostStats(post, interval);
	}

    public StatsData getAuthorStats(String date, Integer authorId) {
        Interval interval = getInterval(date, 30);
        Person person = personRepository.findOne(authorId);

        return analyticsSearchService.getPersonStats(person, interval);
    }

	public StatsData getAuthorStats(String end, String start, Integer authorId) {
		Interval interval = getInterval(end, start);
		Person person = personRepository.findOne(authorId);

		return analyticsSearchService.getPersonStats(person, interval);
	}

	public StatsData getStationStats(String end, String start, Integer stationId){
		Interval interval = getInterval(end, start);
		Station station = stationRepository.findOne(stationId);

		return analyticsSearchService.getStationStats(station, interval);
	}

	public StatsData getNetworkStats(String end, String start) {
		Interval interval = getInterval(end, start);

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		network.setTenantId(tenantId); //it is not supposed to happen

		return analyticsSearchService.getNetworkStats(network, interval);
	}

	public Map<Integer, Integer> getPostReads(List<Integer> postIds){
	    List<Post> posts = postRepository.findAll(postIds);
		return analyticsSearchService.getPostReads(posts);
	}

	public Map<String, Integer> getFileStats(){
        return analyticsSearchService.getFileStats();
	}

	public Map<String, Integer> getStationReaders(Integer stationId){
	    Station station = stationRepository.findOne(stationId);
        Map stationReaders = analyticsSearchService.getReadersByStation(station);

		stationReaders.put("stationId", stationId);

		return stationReaders;
	}

	public Map<String,Integer> dashboardStats() {
		Map<String, Integer> ret = new LinkedHashMap<>();
		Long posts = postRepository.countByState(Post.STATE_PUBLISHED);
		Long comments = commentRepository.count();

		ret.put("post", posts != null ? posts.intValue() : 0);
		ret.put("comment", comments.intValue());

		return ret;
	}
}