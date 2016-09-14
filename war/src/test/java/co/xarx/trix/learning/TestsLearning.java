package co.xarx.trix.learning;

import co.xarx.trix.util.StringUtil;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class TestsLearning {
	public static void main(String[] args){
//		String username = "romero3.com.com";
//		boolean ret = StringUtil.isFQDN(username);
//		System.out.println(new Date(0));

		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH:mm:ssZ");
		Long d = dtf.parseDateTime("2016-09-12-15:00:00-0300").getMillis();
		System.out.println(d.equals(1473703200000L));
		System.out.println(new Date(d));
		System.exit(0);
	}
}
