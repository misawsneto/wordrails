package co.xarx.trix.test.unit;

import co.xarx.trix.api.PageView;
import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.test.TestArtifactsFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

//@RunWith(MockitoJUnitRunner.class)
public class JacksonTest {

	ObjectMapper objectMapper;

//	@Before
	public void setUp() throws Exception {
		objectMapper = new ObjectMapper();
	}

	public void deserializePage() throws IOException {
		PageView view = objectMapper.readValue(new java.io.File("src/test/resources/page.json"), PageView.class);

		System.out.println(view);
	}

	public void serializePage() throws JsonProcessingException, FileNotFoundException {
		Page page = TestArtifactsFactory.createPage();

		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String json = objectMapper.writeValueAsString(page);

		writeToFile("page.json", json);
	}

	private void writeToFile(String fileName, String string) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("src/test/resources/" + fileName);
		writer.print(string);
		writer.close();
	}

	static String readFile(String fileName) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/" + fileName));
		return new String(encoded, StandardCharsets.UTF_8);
	}

	public void termTest() throws IOException {
		Term term = objectMapper.readValue(new java.io.File("src/test/resources/term.json"), Term.class);

		System.out.println(term);
	}

	public void imageTest() throws IOException {
		Image image = createImage();

		String json = objectMapper.writeValueAsString(image);
		System.out.println(json);
	}

	public void testPost() throws IOException {
		String json = readFile("src/test/resources/post.json");
		Post post = objectMapper.readValue(json, Post.class);

		Assert.assertNotNull(post);
	}

	private Image createImage() {
		Image image = new Image(Image.Type.POST);
		image.pictures = Sets.newHashSet(
				new Picture("original", createFile()),
				new Picture("large", createFile()),
				new Picture("medium", createFile()),
				new Picture("small", createFile())
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