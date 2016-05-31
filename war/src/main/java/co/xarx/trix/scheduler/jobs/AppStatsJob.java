package co.xarx.trix.scheduler.jobs;

import co.xarx.trix.domain.ESAppStats;
import co.xarx.trix.domain.PublishedApp;
import co.xarx.trix.persistence.ESAppStatsRepository;
import co.xarx.trix.persistence.PublishedAppRepository;
import co.xarx.trix.util.Constants;
import es.arcadiaconsulting.appstoresstats.android.console.AndroidStoreStats;
import es.arcadiaconsulting.appstoresstats.common.CommonStatsData;
import es.arcadiaconsulting.appstoresstats.common.IStoreStats;
import es.arcadiaconsulting.appstoresstats.ios.console.IOSStoreStats;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static co.xarx.trix.util.Logger.info;

public class AppStatsJob extends QuartzJobBean {
	@Autowired
	public PublishedAppRepository appRepository;
	@Autowired
	public ESAppStatsRepository appStatsRepository;

	public CommonStatsData getAndroidStats(PublishedApp app) {
		IStoreStats play = new AndroidStoreStats();
		return play.getFullStatsForApp(
				app.getPublisherEmail(),
				app.getPublisherPassword(),
				app.getPackageName(),
				null,
				app.getPublisherPublicName());
	}

	public CommonStatsData getIosStats(PublishedApp app) {
		IStoreStats store = new IOSStoreStats();
		return store.getFullStatsForApp(
				app.getPublisherEmail(),
				app.getPublisherPassword(),
				app.getSku(),
				app.getVendorId(),
				null);
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		info("requesting stats" + new Date().toString());

		List<PublishedApp> apps = appRepository.findAll();
		List<ESAppStats> storeStatsDatas = new ArrayList<>();

		if(apps == null || apps.isEmpty()){
			return;
		}

		for(PublishedApp app: apps){
			CommonStatsData stats;

			if(app.getType().equals(Constants.MobilePlatform.ANDROID)){
				stats = getAndroidStats(app);
			} else {
				stats = getIosStats(app);
			}

			ESAppStats appStats = new ESAppStats();
			appStats.averageRaiting = stats.getAverageRate();
			appStats.downloads = stats.getDownloadsNumber();
			appStats.currentInstallations = stats.getCurrentInstallationsNumber();
			appStats.sku = app.sku != null ? app.sku : null;
			appStats.packageName = app.packageName != null ? app.packageName : null;
			appStats.id = app.packageName != null ? app.packageName.hashCode() : app.sku.hashCode();
			appStats.tenantId = app.tenantId;

			storeStatsDatas.add(appStats);
		}
		appStatsRepository.save(storeStatsDatas);
	}
}
