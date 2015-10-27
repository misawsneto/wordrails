package com.wordrails.builder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
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
					System.out.println(line);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}