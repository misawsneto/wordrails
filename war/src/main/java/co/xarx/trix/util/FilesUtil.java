package co.xarx.trix.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FilesUtil {

	public static java.io.File createNewTempFile() throws IOException {
		return java.io.File.createTempFile(TrixUtil.generateRandomString(5, "aA#"), ".tmp");
	}

	public static java.io.File createNewTempFile(InputStream inputStream) throws IOException {
		java.io.File tmpFile = createNewTempFile();
		FileUtils.copyInputStreamToFile(inputStream, tmpFile);
		return tmpFile;
	}

	public static byte[] getBytes(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		org.apache.commons.io.IOUtils.copy(in, baos);
		return baos.toByteArray();
	}

	public static String getHash(InputStream is) throws IOException {
		byte[] bytes = getBytes(is);
		return getHash(bytes);
	}

	public static String getHash(byte[] bytes) throws IOException {
		return DigestUtils.md5Hex(new ByteArrayInputStream(bytes));
	}

	public static InputStream getStreamFromUrl(String url) throws IOException {
		URL fullURL = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) fullURL.openConnection();
		return connection.getInputStream();
	}

	public static File downloadFile(File file, String url) throws IOException {
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

		return file;
	}
}
