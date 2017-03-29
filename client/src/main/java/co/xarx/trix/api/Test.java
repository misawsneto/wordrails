package co.xarx.trix.api;

import retrofit.RestAdapter.LogLevel;

import java.io.File;
import java.io.IOException;

public class Test {

	private static Trix getLocal() throws IOException {
		return new Trix(
				null,
				new MockConnectivityManager(true),
				new File("."), 0,
				"http://sport.xarxlocal.com", "sport", "Sport@dmiN",
				LogLevel.FULL);
	}

	public static void main(String[] args) throws IOException {
		Trix wordRails = getLocal();
		wordRails.login();
		wordRails.getInitialData();
	}
}