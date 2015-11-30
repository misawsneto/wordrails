package co.xarx.trix.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern FQDN_PATTERN = Pattern.compile("(?=^.{1,254}$)(^(?:(?!\\d+\\.|-)[a-zA-Z0-9_\\-]{1,63}(?<!-)\\.?)+(?:[a-zA-Z]{2,})$)", Pattern.CASE_INSENSITIVE);


	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

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

	/**
	 * Converts a string to a slug version by removing special characters and spaces
	 *
	 * @param input - the string to be converted
	 * @return the converted string
	 */
	public static String toSlug(String input) {
		input = input.trim();
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		slug = slug.trim().replaceAll("-+", "-");
		return slug.toLowerCase(Locale.ENGLISH);
	}

	public static boolean isEmailAddr(String emailAddr) {
		try {
			Matcher matcher = EMAIL_PATTERN.matcher(emailAddr);
			return matcher.matches();
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isFQDN(String fqdnVal) {
		try {
			Matcher matcher = FQDN_PATTERN.matcher(fqdnVal);
			return matcher.matches();
		} catch (Exception e) {
		}
		return false;
	}
}
