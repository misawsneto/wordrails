package co.xarx.trix.services.analytics;

import co.xarx.trix.domain.PublishedApp;
import co.xarx.trix.util.StoreStatsData;
import es.arcadiaconsulting.appstoresstats.android.console.AndroidStoreStats;
import es.arcadiaconsulting.appstoresstats.common.CommonStatsData;
import es.arcadiaconsulting.appstoresstats.ios.console.IOSStoreStats;

@lombok.Getter
@lombok.Setter
public class StoreStats {

	PublishedApp appPublished;

	public StoreStats(PublishedApp appPublished){
		this.appPublished = appPublished;
	}

	public StoreStatsData getStoreStats(){

		CommonStatsData stats;

		if(appPublished.getSku() == null || appPublished.getSku().isEmpty()){
//			CommonStatsData android = fetchAndroid.getFullStatsForApp("mobile@xarx.co", "X@rxM0b!l3", "com.wordrails.sportclubdorecife", null, "XARX");
//			this.ios = fetchIOs.getFullStatsForApp("ac@adrielcafe.com", "X@rxtr1x", "SPORTCLUBDORECIFE", "86672524", null);
			AndroidStoreStats fetchAndroid = new AndroidStoreStats();
			stats = fetchAndroid.getFullStatsForApp(appPublished.getPublisherEmail(),
					appPublished.getPublisherPassword(), appPublished.getPackageName(), null, appPublished.getPublisherPublicName());
		} else {
			IOSStoreStats fetchIos = new IOSStoreStats();
			stats = fetchIos.getFullStatsForApp(appPublished.getPublisherEmail(),
					appPublished.getPublisherPassword(), appPublished.getSku(), appPublished.getVendorId(), null);
		}

		StoreStatsData appStats = new StoreStatsData();
		appStats.ratingNumber = stats.getRatings() != null ? stats.getRatings().size() : 0;
		appStats.averageRaiting = stats.getAverageRate();
		appStats.downloadsNumber = stats.getDownloadsNumber();
		appStats.currentInstallationsNumber = stats.getCurrentInstallationsNumber();

		return appStats;
	}
}
