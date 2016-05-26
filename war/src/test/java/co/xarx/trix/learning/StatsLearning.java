package co.xarx.trix.learning;

import es.arcadiaconsulting.appstoresstats.android.console.AndroidStoreStats;
import es.arcadiaconsulting.appstoresstats.common.CommonStatsData;
import es.arcadiaconsulting.appstoresstats.common.IStoreStats;
import es.arcadiaconsulting.appstoresstats.ios.console.IOSStoreStats;
import org.joda.time.DateTime;

public class StatsLearning {
	static IStoreStats fetchAndroid = new AndroidStoreStats();
	static IStoreStats fetchIOs = new IOSStoreStats();

	static CommonStatsData android = fetchAndroid.getFullStatsForApp("mobile@xarx.co", "X@rxM0b!l3", "com.wordrails.sportclubdorecife", null, "XARX");
//	static CommonStatsData ios = fetchIOs.getFullStatsForApp("ac@adrielcafe.com", "X@rxtr1x", "SPORTCLUBDORECIFE", "86672524", null);

	public static void main(String[] args){
		System.out.println(new DateTime().toString());
		System.out.println(android.getDownloadsNumber());
//		System.out.println(ios);
		System.exit(0);
	}
}
