package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static co.xarx.trix.util.AnalyticsUtil.getInterval;
import static co.xarx.trix.util.Logger.debug;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class StatisticsService {

	private PostRepository postRepository;
	private PersonRepository personRepository;
	private NetworkRepository networkRepository;
	private CommentRepository commentRepository;
	private StationRepository stationRepository;
    private AnalyticsSearchService analyticsSearchService;

	@Autowired
	public StatisticsService(
			PostRepository postRepository,
			PersonRepository personRepository,
			NetworkRepository networkRepository, CommentRepository commentRepository,
			StationRepository stationRepository,
			AnalyticsSearchService analyticsSearchService) {
		this.postRepository = postRepository;
		this.personRepository = personRepository;
		this.networkRepository = networkRepository;
		this.stationRepository = stationRepository;
		this.analyticsSearchService = analyticsSearchService;
        this.commentRepository = commentRepository;
	}

	public Map getMostPupularNetworks(Integer size){
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

		if (post == null) return null;

        return analyticsSearchService.getPostStats(post, interval);
    }

	public StatsData getPostStats(String end, String beginning, Integer postId){
		Interval interval = getInterval(end, beginning);
		Post post = postRepository.findOne(postId);

		if (post == null) return null;

		return analyticsSearchService.getPostStats(post, interval);
	}

    public StatsData getAuthorStats(String date, Integer authorId) {
        Interval interval = getInterval(date, 30);
        Person person = personRepository.findOne(authorId);

		if (person == null) return null;

        return analyticsSearchService.getPersonStats(person, interval);
    }

	public StatsData getAuthorStats(String end, String start, Integer authorId) {
		Interval interval = getInterval(end, start);
		Person person = personRepository.findOne(authorId);

		if (person == null) return null;

		return analyticsSearchService.getPersonStats(person, interval);
	}

	public StatsData getStationStats(String end, String start, Integer stationId){
		Interval interval = getInterval(end, start);
		Station station = stationRepository.findOne(stationId);

		if (station == null) return null;

		return analyticsSearchService.getStationStats(station, interval);
	}

	public StatsData getNetworkStats(String end, String start){
		Interval interval = getInterval(end, start);

		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());
		if (network == null) return null;

		network.setTenantId(TenantContextHolder.getCurrentTenantId());
		return analyticsSearchService.getNetworkStats(network, interval);
	}

	public Map<Integer, Integer> getPostReads(List<Integer> postIds){
	    List<Post> posts = postRepository.findAll(postIds);

		if(posts == null || posts.isEmpty()) return null;

		return analyticsSearchService.getPostReads(posts);
	}

	public Map<String, Integer> getFileStats(){
        return analyticsSearchService.getFileStats();
	}

	public Map<String, Integer> getStationReaders(Integer stationId){
	    Station station = stationRepository.findOne(stationId);
		if (station == null) return null;

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