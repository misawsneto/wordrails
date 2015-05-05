package com.wordrails.util;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

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
		if (chars.contains("u")) mask += "~`!@#$%^*()_+-={}[]:\";\'<>,.|";
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
}