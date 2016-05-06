package co.xarx.trix.util;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class StringUtilTest {

	public Map<String, Boolean> batchEmails;

	@Before
	public void setUp() throws Exception {
		batchEmails = new HashMap<>();
		batchEmails.put("email@example.com", true);
		batchEmails.put("firstname.lastname@example.com", true);
		batchEmails.put("email@subdomain.example.com", true);
		batchEmails.put("firstname+lastname@example.com", true);
		batchEmails.put("email@[123.123.123.123]", true);
		batchEmails.put("“email”@example.com", true);
		batchEmails.put("1234567890@example.com", true);
		batchEmails.put("email@example-one.com", true);
		batchEmails.put("_______@example.com", true);
		batchEmails.put("email@example.name", true);
		batchEmails.put("email@example.museum", true);
		batchEmails.put("email@example.co.jp", true);
		batchEmails.put("firstname-lastname@example.com", true);

		batchEmails.put("plainaddress", false);
		batchEmails.put("#@%^%#$@#$@#.com", false);
		batchEmails.put("@example.com", false);
		batchEmails.put("Joe Smith <email@example.com>", false);
		batchEmails.put("email.example.com", false);
		batchEmails.put("email@example@example.com", false);
		batchEmails.put(".email@example.com", false);
		batchEmails.put("email.@example.com", false);
		batchEmails.put("email..email@example.com", false);
		batchEmails.put("email@example.com (Joe Smith)", false);
		batchEmails.put("email@example", false);
		batchEmails.put("email@-example.com", false);
		batchEmails.put("email@example.web", false);
		batchEmails.put("email@111.222.333.44444", false);
		batchEmails.put("email@example..com", false);
		batchEmails.put("Abc..123@example.com", false);
	}

	@Test
	public void testToSlug() throws Exception {

	}

	@Test
	public void testIsEmailAddr() throws Exception {
		Map<String, Boolean> testEmails = new HashMap<>();
		batchEmails.forEach((email, isValid) -> {
			testEmails.put(email, StringUtil.isEmailAddr(email));
			if(testEmails.get(email) != isValid){
				System.out.println(email + " " + testEmails.get(email) + " " + isValid);
			}
		});
		assertEquals(batchEmails, testEmails);
	}
}