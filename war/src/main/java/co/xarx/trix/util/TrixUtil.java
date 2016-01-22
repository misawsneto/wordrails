package co.xarx.trix.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrixUtil {

	public enum EntityType{POST,PERSON,PERSPECTIVE}

	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static boolean urlExists(String urlString) throws IOException {
		URL u = new URL(urlString);
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
		huc.setRequestMethod("HEAD");
		huc.connect();
		return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
	}

	/**
	 * Returns consecutive {@linkplain List#subList(int, int) sublists} of a list,
	 * each of the same size (the final list may be smaller). For example,
	 * partitioning a list containing {@code [a, b, c, d, e]} with a partition
	 * size of 3 yields {@code [[a, b, c], [d, e]]} -- an outer list containing
	 * two inner lists of three and two elements, all in the original order.
	 * <p/>
	 * <p>The outer list is unmodifiable, but reflects the latest state of the
	 * source list. The inner lists are sublist views of the original list,
	 * produced on demand using {@link List#subList(int, int)}, and are subject
	 * to all the usual caveats about modification as explained in that API.
	 * <p/>
	 * * Adapted from http://code.google.com/p/google-collections/
	 *
	 * @param list the list to return consecutive sublists of
	 * @param size the desired size of each sublist (the last may be
	 *             smaller)
	 * @return a list of consecutive sublists
	 * @throws IllegalArgumentException if {@code partitionSize} is nonpositive
	 */
	public static <T> List<List<T>> partition(List<T> list, int size) {

		if (list == null) throw new NullPointerException("'list' must not be null");
		if (!(size > 0)) throw new IllegalArgumentException("'size' must be greater than 0");

		return new Partition<T>(list, size);
	}

	private static class Partition<T> extends AbstractList<List<T>> {

		final List<T> list;
		final int size;

		Partition(List<T> list, int size) {
			this.list = list;
			this.size = size;
		}

		@Override
		public List<T> get(int index) {
			int listSize = size();
			if (listSize < 0) throw new IllegalArgumentException("negative size: " + listSize);
			if (index < 0) throw new IndexOutOfBoundsException("index " + index + " must not be negative");
			if (index >= listSize)
				throw new IndexOutOfBoundsException("index " + index + " must be less than size " + listSize);
			int start = index * size;
			int end = Math.min(start + size, list.size());
			return list.subList(start, end);
		}

		@Override
		public int size() {
			return (list.size() + size - 1) / size;
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}
	}
}
