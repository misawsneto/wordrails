package co.xarx.trix.api;

import retrofit.RestAdapter.LogLevel;

import java.io.File;
import java.io.IOException;

public class Test {

	private static WordRails getLocal() throws IOException {
		return new WordRails(
				new MockConnectivityManager(true),
				new File("."), 0,
				"http://sport.xarxlocal.com", "sport", "Sport@dmiN",
				LogLevel.FULL);
	}

	public static void main(String[] args) throws IOException {
		//WordRails wordRails = getTest("demo", "demo");
		WordRails wordRails = getLocal();
		wordRails.login();
		wordRails.getInitialData();

	}
}