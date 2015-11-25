package co.xarx.trix.util;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrixUtil {

	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	public enum EntityType{POST,PERSON,PERSPECTIVE};

    private static final Pattern emailPattern = Pattern.compile("^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern hostPattern = Pattern.compile("^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$", Pattern.CASE_INSENSITIVE);

    private static final Pattern fqdnPattern = Pattern.compile("(?=^.{1,254}$)(^(?:(?!\\d+\\.|-)[a-zA-Z0-9_\\-]{1,63}(?<!-)\\.?)+(?:[a-zA-Z]{2,})$)", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailAddr(String emailAddr){
        try {
            Matcher matcher = emailPattern.matcher(emailAddr);
            return matcher.matches();
        } catch (Exception e){
        }
        return false;
    }

    public static boolean isFQDN(String fqdnVal){
        try {
            Matcher matcher = fqdnPattern.matcher(fqdnVal);
            return matcher.matches();
        } catch (Exception e){
        }
        return false;
    }

    public static boolean isHost (String hostname){
        try {
            Matcher matcher = hostPattern.matcher(hostname);
            return matcher.matches();
        } catch (Exception e){
        }
        return false;
    }

	/**
	 * Converts a string to a slug version by removing special characters and spaces
	 *
	 * @param input - the string to be converted
	 * @return the converted string
	 */
	public static String toSlug(String input) {
		input = input.trim();
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		slug = slug.trim().replaceAll("-+", "-");
		return slug.toLowerCase(Locale.ENGLISH);
	}

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

	public static String generateRandomString(int length, String chars) {
		String mask = "";
		if (chars.contains("a")) mask += "abcdefghijklmnopqrstuvwxyz";
		if (chars.contains("A")) mask += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (chars.contains("#")) mask += "0123456789";
		if (chars.contains("!")) mask += "~`!@#$%^&*()_+-={}[]:\";\'<>?,./|\\";
		if (chars.contains("u")) mask += "~!@$^*()_+-=:\";\',.|"; //unsafe -> < > # % { } | \ ^ ~ [ ] `
		String result = "";
		for (int i = length; i > 0; --i) {
			int index = (int) Math.round(Math.random() * (mask.length() - 1));
			result += mask.charAt(index);
		}

		return result;
	}

	public static void nullifyProperties(Object o) {
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (!java.lang.reflect.Modifier.isStatic(f.getModifiers()) && !f.getType().isPrimitive())
					f.set(o, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static String simpleSnippet(String body, int max) {
		String[] splitPhrase = body.split("\\s+");
		int limit = splitPhrase.length >= 100 ? 100 : splitPhrase.length;
		String string = StringUtils.join(Arrays.copyOfRange(splitPhrase, 0, limit), " ");
		Document doc = Jsoup.parse(string);
		return doc.text();
	}

	public static String htmlStriped(String body) {
		Document doc = Jsoup.parse(body);
		return doc.text();
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
