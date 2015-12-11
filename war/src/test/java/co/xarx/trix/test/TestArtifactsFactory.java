package co.xarx.trix.test;

import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.BaseSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.domain.query.PostQuery;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.RandomStringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.TreeMap;

public class TestArtifactsFactory {

	public static final Integer NETWORK = 5;
	public static final String TENANT = "dummy";


	public static Post createPost() {
		Post post = new Post();
		post.id = 5;
		post.setNetworkId(NETWORK);
		post.setTenantId(TENANT);
		post.title = "Dummy title";
		post.body = "Dummy body";
		post.topper = "Dummy topper";
		post.slug = "dummy-slug";
		post.station = createStation();
		post.author = createPerson();
		post.terms = Sets.newHashSet(createTerm("cat1"), createTerm("cat2"));
		post.tags = Sets.newHashSet("dummy1", "dummy2");
		post.date = new Date();
		post.readsCount = 3;
		post.commentsCount = 4;
		post.recommendsCount = 5;
		post.bookmarksCount = 6;
		post.readTime = 9;
		post.featuredImage = createImage(Image.Type.POST);
		post.featuredVideoHash = generateToken();
		post.featuredAudioHash = generateToken();
		post.lat = 132.51;
		post.lng = 42.90;
		post.subheading = "Dummy subheading";
		post.scheduledDate = new Date();
		post.notify = true;
		return post;
	}

	public static Image createImage(Image.Type type) {
		Image image = new Image();
		for (String s : type.getSizeTags()) {
			image.hashs.put(s, generateToken());
		}
		image.caption = "Dummy caption";
		image.credits = "Dummy credits";
		image.title = "Dummy title";

		return image;
	}

	private static String generateToken() {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		StringBuilder hexString = new StringBuilder();
		byte[] data = md.digest(RandomStringUtils.randomAlphabetic(10).getBytes());
		for (byte aData : data) {
			hexString.append(Integer.toHexString((aData >> 4) & 0x0F));
			hexString.append(Integer.toHexString(aData & 0x0F));
		}
		return hexString.toString();
	}

	public static Term createTerm(String name) {
		Term term = new Term();
		term.id = 7;
		term.name = name;
		return term;
	}

	public static Person createPerson() {
		Person person = new Person();
		person.setNetworkId(NETWORK);
		person.setTenantId(TENANT);
		person.id = 10;
		person.name = "Dummy Person";
		person.email = "dummy@dummy.com";
		person.username = "dummyuser";
		person.twitterHandle = "dummytwitteraccount";
		person.cover = createImage(Image.Type.COVER);
		person.image = createImage(Image.Type.PROFILE_PICTURE);
		return person;
	}


	public static Station createStation() {
		Station station = new Station();
		station.id = 11;
		station.setNetworkId(NETWORK);
		station.setTenantId(TENANT);
		station.name = "TUPY";
		station.visibility = "UNRESTRICTED";
		station.logo = createImage(Image.Type.LOGIN);
		station.logo.id = 16119;
		station.logoId = 3722;
		station.defaultPerspectiveId = 7;
		station.categoriesTaxonomyId = 140;
		station.tagsTaxonomyId = 183;
		return station;
	}

	public static Page createPage() {
		Page page = new Page();
		page.setNetworkId(NETWORK);
		page.setTenantId(TENANT);
		page.setTitle("Home");

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

		page.setSections(new TreeMap<Integer, BaseSection>() {{
			put(0, section1);
			put(1, section2);
		}});

		return page;
	}
}
