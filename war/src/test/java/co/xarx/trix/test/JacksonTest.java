package co.xarx.trix.test;

import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.BaseSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
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
import java.util.TreeMap;

@ContextConfiguration({
		"classpath:applicationContext-test.xml"
})
public class JacksonTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	ObjectMapper objectMapper;

	@Test
	public void serializePage() throws JsonProcessingException {
		ElasticSearchQuery q1 = new ElasticSearchQuery();
		q1.setQueryString("{ \"bool\" : { \"must\" : [ { \"multi_match\" : { \"query\" : \"dilma\", \"fields\" : [ \"body^2.0\", \"title^5.0\", \"topper\", \"subheading\", \"authorName\", \"terms.name\" ], \"prefix_length\" : 1 } }, { \"match\" : { \"state\" : { \"query\" : \"PUBLISHED\", \"type\" : \"boolean\" } } }, { \"bool\" : { \"should\" : [ { \"match\" : { \"stationId\" : { \"query\" : \"11\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"14\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"55\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"75\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"76\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"77\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"137\", \"type\": \"boolean\" } } }]} } ]} }");
		q1.setObjectName("post");
		q1.setHighlightedField("body");

		PageableQuery pageableQuery = new PageableQuery();
		pageableQuery.setElasticSearchQuery(q1);

		ElasticSearchQuery q2 = new ElasticSearchQuery();
		q2.setQueryString("{ \"bool\" : { \"must\" : [ { \"multi_match\" : { \"query\" : \"fhc\", \"fields\" : [ \"body^2.0\", \"title^5.0\", \"topper\", \"subheading\", \"authorName\", \"terms.name\" ], \"prefix_length\" : 1 } }, { \"match\" : { \"state\" : { \"query\" : \"PUBLISHED\", \"type\" : \"boolean\" } } }, { \"bool\" : { \"should\" : [ { \"match\" : { \"stationId\" : { \"query\" : \"11\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"14\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"55\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"75\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"76\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"77\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"137\", \"type\": \"boolean\" } } }]} } ]} }");
		q2.setObjectName("post");
		q2.setHighlightedField("body");
		FixedQuery fixedQuery1 = new FixedQuery();
		fixedQuery1.setElasticSearchQuery(q2);
		fixedQuery1.setIndexes(Sets.newHashSet(0, 2, 3));

		FixedQuery fixedQuery2 = new FixedQuery();
		fixedQuery2.setElasticSearchQuery(q2);
		fixedQuery2.setIndexes(Sets.newHashSet(0, 1, 2, 3, 4));

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

		Page page = new Page();
		page.setTitle("Home");
		page.setStation(station);

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

		String json = objectMapper.writeValueAsString(page);

		System.out.println(json);
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
