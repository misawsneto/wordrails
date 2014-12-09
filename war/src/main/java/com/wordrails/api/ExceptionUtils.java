package com.wordrails.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	public static String getStackTrace(Throwable throwable) {		
		StringWriter stringWriter = new StringWriter();
		try {
			PrintWriter printWriter = new PrintWriter(stringWriter);
			try {
				throwable.printStackTrace(printWriter);				
			} finally {
				printWriter.close();
			}			
		} finally {
			try {
				stringWriter.close();
			} catch (IOException e) {}
		}
		return stringWriter.toString();
	}
}