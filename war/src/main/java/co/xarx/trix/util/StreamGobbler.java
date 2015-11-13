package co.xarx.trix.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler implements Runnable {

	Logger log = Logger.getLogger(StreamGobbler.class.getName());

	private InputStream is;
	private boolean showLog;

	public StreamGobbler(InputStream is, boolean showLog) {
		this.is = is;
		this.showLog = showLog;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (showLog) {
					log.debug(line);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}