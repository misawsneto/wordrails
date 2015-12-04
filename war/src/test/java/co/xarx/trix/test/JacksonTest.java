package co.xarx.trix.test;

import co.xarx.trix.api.PageView;
import co.xarx.trix.config.spring.ApplicationConfig;
import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.BaseSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.domain.query.PostQuery;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

@ContextConfiguration(classes = {
		ApplicationConfig.class
})
public class JacksonTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	ObjectMapper objectMapper;

	@Test
	public void deserializePage() throws IOException {
		PageView view = objectMapper.readValue(new java.io.File("src/test/resources/page.json"), PageView.class);

		System.out.println(view);
	}

	@Test
	public void serializePage() throws JsonProcessingException, FileNotFoundException {
		PostQuery postQuery1 = new PostQuery();
		postQuery1.setStationIds(Lists.newArrayList(11));
		postQuery1.setRichText("dilma");

		PageableQuery pageableQuery = new PageableQuery();
		pageableQuery.setObjectQuery(postQuery1);

		PostQuery postQuery2 = new PostQuery();
		postQuery2.setStationIds(Lists.newArrayList(11));
		postQuery2.setRichText("fhc");

		FixedQuery fixedQuery1 = new FixedQuery();
		fixedQuery1.setObjectQuery(postQuery2);
		fixedQuery1.setIndexes(Sets.newHashSet(0, 2, 3));

		FixedQuery fixedQuery2 = new FixedQuery();
		fixedQuery2.setObjectQuery(postQuery2);
		fixedQuery2.setIndexes(Sets.newHashSet(0, 1, 2, 3, 4));

		Page page = new Page();
		page.setTitle("Home");

		QueryableListSection section1 = new QueryableListSection();
		section1.setTitle("Section 1");
		section1.setSize(10);
		section1.setPageable(true);
		section1.setPageableQuery(pageableQuery);
		section1.setFixedQueries(Lists.newArrayList(fixedQuery1));

		QueryableListSection section2 = new QueryableListSection();
		section2.setTitle("Section 2");
		section2.setSize(5);
		section2.setPageable(false);
		section2.setFixedQueries(Lists.newArrayList(fixedQuery2));

		page.setSections(new TreeMap<Integer, BaseSection>() {{put(0, section1); put(1, section2);}});

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

	private Station newStation() {
		Station station = new Station();
		station.id = 11;
		station.name = "TUPY";
		station.visibility = "UNRESTRICTED";
		station.logo = new Image();
		station.logo.id = 16119;
		station.logoId = 3722;
		station.defaultPerspectiveId = 7;
		station.categoriesTaxonomyId = 140;
		station.tagsTaxonomyId = 183;
		station.networkId = 5;
		return station;
	}

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
		String json = readFile("src/test/resources/post.json");
		Post post = objectMapper.readValue(json, Post.class);

		Assert.assertNotNull(post);
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
