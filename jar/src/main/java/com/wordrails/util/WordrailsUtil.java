package com.wordrails.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.Iterator;
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

	public static String simpleSnippet(String body){
		String[] splitPhrase = body.split("\\s+");
		int limit = splitPhrase.length >= 100 ? 100 : splitPhrase.length;
		String string = StringUtils.join(Arrays.copyOfRange(splitPhrase, 0, limit), " ");
		Document doc = Jsoup.parse(string);
		return doc.text();
	}

	public static WordpressParsedContent extractImageFromContent(String content){
		if(content == null || content.isEmpty()){
			return null;
		}
		Document doc = Jsoup.parse(content);
		// Get all img tags
		String featuredImage = null;
		Elements imgs = doc.getElementsByTag("img");
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
		
		WordpressParsedContent wpc = new WordpressParsedContent();
		wpc.content = doc.text();
		wpc.featuredImage = featuredImage;
		
		wpc.content = wpc.content.replaceAll("\\[(.*?)\\](.*?)\\[/(.*?)\\]", "");
		wpc.content = wpc.content.trim();

		return wpc;
	}
}