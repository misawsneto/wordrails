package co.xarx.trix.test;

import co.xarx.trix.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ContextConfiguration({
		"classpath:applicationContext-test.xml"
})
public class JacksonTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	ObjectMapper objectMapper;

	@Test
	public void termTest() throws IOException {
		Term term = objectMapper.readValue(new java.io.File("src/test/resources/term.json"), Term.class);

		System.out.println(term);
	}

	@Test
	public void imageTest() throws IOException {
		Image image = createImage();

		String json = objectMapper.writeValueAsString(image);
		System.out.println(json);
	}

	@Test
	public void testPost() throws IOException {
		String json = readFile("src/test/resources/post.json", StandardCharsets.UTF_8);
		Post post = objectMapper.readValue(json, Post.class);

		Assert.assertNotNull(post);
	}

	static String readFile(String path, Charset encoding)
			throws IOException
	{
		Path path1 = Paths.get(path);
		System.out.println(path1.toAbsolutePath().toString());
		byte[] encoded = Files.readAllBytes(path1);
		return new String(encoded, encoding);
	}

	private Image createImage() {
		Image image = new Image();
		image.original = createFile();
		image.large = createFile();
		image.medium = createFile();
		image.small = createFile();
		image.pictures = Sets.newHashSet(
				new Picture("original", image.original),
				new Picture("large", image.large),
				new Picture("medium", image.medium),
				new Picture("small", image.small)
		);

		image.create();

		return image;
	}

	private File createFile() {
		File file = new File();
		file.directory = "images";
		file.hash = "fff862d6283b4689884904505240e61f";
		file.mime = "image/jpg";
		file.type = "E";

		return file;
	}
}
