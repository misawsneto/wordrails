package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.CommentData;
import co.xarx.trix.api.v2.MobileStats;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.query.statement.CommentStatement;
import co.xarx.trix.persistence.CommentRepository;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.PublishedAppRepository;
import co.xarx.trix.services.comment.CommentSearchService;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.ImmutablePage;
import com.google.common.collect.Lists;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.xarx.trix.config.security.Permissions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static co.xarx.trix.util.AnalyticsUtil.getInterval;

@Service
public class AnalyticsQueries {

    private FileRepository fileRepository;
	private MobileDeviceRepository deviceRepository;
    private CommentSearchService commentSearchService;
	private Map<Class<?>, String> commentSearchFields;
	private Map<Class<?>, String> recommendSearchFields;
    private PersonPermissionService personPermissionService;
    private Map<Constants.MobilePlatform, String> mobileTypesMapToString;

	@Autowired
	public AnalyticsQueries(MobileDeviceRepository deviceRepository, CommentSearchService commentSearchService, FileRepository fileRepository, PersonPermissionService personPermissionService){
		this.deviceRepository = deviceRepository;
		this.commentSearchService = commentSearchService;
		this.fileRepository = fileRepository;
		this.personPermissionService = personPermissionService;
		this.commentSearchFields = loadCommentStatementField();

		mobileTypesMapToString = new HashMap<>();
		mobileTypesMapToString.put(Constants.MobilePlatform.ANDROID, "ANDROID");
		mobileTypesMapToString.put(Constants.MobilePlatform.APPLE, "APPLE");
	}

	private Map loadCommentStatementField() {
		Map fields = new HashMap<Class<?>, String>();

		fields.put(Post.class.getName(), "posts");
		fields.put(Person.class.getName(), "authors");
		fields.put(Station.class.getName(), "stations");
		fields.put(Network.class.getName(), "tenants");

		return fields;
	}

	public Integer countRecommendsByEntity(AnalyticsEntity entity){
		return null;
	}

	public List<MobileStats> getMobileStats(String tenantId, Interval interval){
		List<MobileStats> stats = new ArrayList<>();

		Interval week = getInterval(interval.getEnd(), 7);
		Interval month = getInterval(interval.getEnd(), 30);

		for(Constants.MobilePlatform app: Constants.MobilePlatform.values()){
			MobileStats appStats = new MobileStats();

			appStats.type = app;
			appStats.currentInstallations = (int) (long) deviceRepository.countDevicesByTenantIdAndType(tenantId, app);
			appStats.weeklyActiveUsers = getActiveUserByInterval(week, app);
			appStats.monthlyActiveUsers = getActiveUserByInterval(month, app);

			stats.add(appStats);
		}

		return stats;
	}

	public Integer getActiveUserByInterval(Interval interval, Constants.MobilePlatform type){
		return (int) (long) deviceRepository.countActiveDevices(
				TenantContextHolder.getCurrentTenantId(),
				type, interval.getStart().toString(),
				interval.getEnd().toString());
	}


	public Map getCommentsByEntity(AnalyticsEntity entity){
		CommentStatement params = buildStatement(entity);
		ImmutablePage<CommentData> comments = commentSearchService.search(params, null, null);

		Map<Long, Long> hist = new HashMap();
		for(CommentData c: comments){
			long date = c.getDate().getTime();

			if(hist.get(date) == null){
				hist.put(date, 1L);
			} else {
				long i = hist.get(date);
				hist.put(date, i + 1);
			}
		}

		return hist;
	}

	public Integer countCommentsByEntity(AnalyticsEntity entity){
		CommentStatement params = buildStatement(entity);
		return commentSearchService.search(params, null, null).size();
	}

	public CommentStatement buildStatement(AnalyticsEntity entity){
		List<Integer> entityId = Lists.newArrayList(entity.getId());
		String fieldName = commentSearchFields.get(entity.getClass().getName());
		Field field = null;

		try {
			field = CommentStatement.class.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		field.setAccessible(true);

		CommentStatement params = new CommentStatement();

		try {
			field.set(params, entityId);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return params;
	}

	public Map<String, Integer> getFileStats() {
		List<Object[]> mimeSums = fileRepository.sumFilesSizeByMime(TenantContextHolder.getCurrentTenantId());
		Map<String, Integer> map = new HashMap<>();

		mimeSums.stream().filter(tuple -> tuple[0] != null && tuple[1] != null)
				.forEach(tuple -> map.put((String) tuple[0], (int) (long) tuple[1]));

		return map;
	}

	public List<Integer> getPersonIdsFromStation(Integer stationId){
		List<Person> persons = personPermissionService.getPersonFromStation(stationId, Permissions.READ);
		return persons.stream().map(Person::getId).collect(Collectors.toList());
	}
}
