package com.wordrails.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WordrailsUtil {

	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	/**
	 * Converts a string to a slug version by removing special characters and spaces
	 * @param input - the string to be converted
	 * @return the converted string
	 * */
	public static String toSlug(String input) {
		input = input.trim();
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		slug = slug.trim().replaceAll("-+", "-");
		return slug.toLowerCase(Locale.ENGLISH);
	}

	public static String generateRandomString(int length, String chars) {
		String mask = "";
		if (chars.contains("a")) mask += "abcdefghijklmnopqrstuvwxyz";
		if (chars.contains("A")) mask += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (chars.contains("#")) mask += "0123456789";
		if (chars.contains("!")) mask += "~`!@#$%^&*()_+-={}[]:\";\'<>?,./|\\";
		if (chars.contains("u")) mask += "~!@$^*()_+-=:\";\',.|"; //unsafe -> < > # % { } | \ ^ ~ [ ] `
		String result = "";
		for (int i = length; i > 0; --i){
			int index = (int) Math.round(Math.random() * (mask.length() - 1));
			result += mask.charAt(index);
		}

		return result;
	}

	public static void nullifyProperties( Object o ) {
		for ( Field f : o.getClass().getDeclaredFields() ) {
			f.setAccessible(true);
			try {
				if(!java.lang.reflect.Modifier.isStatic(f.getModifiers()) && !f.getType().isPrimitive())
					f.set( o , null);
			} catch ( Exception e ) { 
				throw new RuntimeException(e);
			}
		}
	}

	public static String simpleSnippet(String body, int max){
		max = max > 0 ? max : 100; 
		String[] splitPhrase = body.split("\\s+");
		int limit = splitPhrase.length >= 100 ? 100 : splitPhrase.length;
		String string = StringUtils.join(Arrays.copyOfRange(splitPhrase, 0, limit), " ");
		Document doc = Jsoup.parse(string);
		return doc.text();
	}

	/**
	 * Returns consecutive {@linkplain List#subList(int, int) sublists} of a list,
	 * each of the same size (the final list may be smaller). For example,
	 * partitioning a list containing {@code [a, b, c, d, e]} with a partition
	 * size of 3 yields {@code [[a, b, c], [d, e]]} -- an outer list containing
	 * two inner lists of three and two elements, all in the original order.
	 *
	 * <p>The outer list is unmodifiable, but reflects the latest state of the
	 * source list. The inner lists are sublist views of the original list,
	 * produced on demand using {@link List#subList(int, int)}, and are subject
	 * to all the usual caveats about modification as explained in that API.
	 *
	 * * Adapted from http://code.google.com/p/google-collections/ 
	 *
	 * @param list the list to return consecutive sublists of
	 * @param size the desired size of each sublist (the last may be
	 *     smaller)
	 * @return a list of consecutive sublists
	 * @throws IllegalArgumentException if {@code partitionSize} is nonpositive
	 */
	public static <T> List<List<T>> partition(List<T> list, int size) {

		if (list == null)
			throw new NullPointerException("'list' must not be null");
		if (!(size > 0))
			throw new IllegalArgumentException("'size' must be greater than 0");

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
			if (listSize < 0)
				throw new IllegalArgumentException("negative size: " + listSize);
			if (index < 0)
				throw new IndexOutOfBoundsException("index " + index + " must not be negative");
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

	public static WordpressParsedContent extractImageFromContent(String content){
		if(content == null || content.isEmpty()){
			return null;
		}
		Document doc = Jsoup.parse(content);
		// Get all img tags
		String featuredImage = null;
		Elements imgs = doc.getElementsByTag("img");

		try{
			for (Element element : imgs) {
				String imageURL = element.attr("src");
				if(imageURL != null && !imageURL.isEmpty()){
					URL url;
					try {
						url = new URL(imageURL);
						try(InputStream is = url.openStream()){
							try(ImageInputStream in = ImageIO.createImageInputStream(is)){
								final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
								if (readers.hasNext()) {
									ImageReader reader = readers.next();
									try {
										reader.setInput(in);
										int dimensions = reader.getWidth(0) * reader.getHeight(0);
										if(dimensions > 250000){
											Element parent = element.parent();
											if(parent != null && parent.tagName().equals("a")){
												parent.remove();
											}
											featuredImage = imageURL;
										}
									} finally {
										reader.dispose();
									}
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}catch (IOException e) {
							e.printStackTrace();
						}
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		WordpressParsedContent wpc = new WordpressParsedContent();
		wpc.content = doc.text();
		wpc.externalImageUrl = featuredImage;

		wpc.content = wpc.content.replaceAll("\\[(.*?)\\](.*?)\\[/(.*?)\\]", "");
		wpc.content = wpc.content.trim();

		return wpc;
	}
}