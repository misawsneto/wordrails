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
    private PersonPermissionService personPermissionService;

	@Autowired
	public AnalyticsQueries(MobileDeviceRepository deviceRepository, FileRepository fileRepository, PersonPermissionService personPermissionService){
		this.deviceRepository = deviceRepository;
		this.fileRepository = fileRepository;
		this.personPermissionService = personPermissionService;
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

	public Map<String, Integer> getFileStats(){
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
