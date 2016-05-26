package co.xarx.trix;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.*;
import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.RandomStringUtils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TestArtifactsFactory {

	public static final String TENANT = "dummy";


	public static Post createPost() {
		Post post = new Post();
		post.id = 5;
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
		post.externalVideoUrl = "https://www.youtube.com/watch?v=jXhFcBIEDHM";
		post.subheading = "Dummy subheading";
		post.scheduledDate = new Date();
		post.notify = true;
		post.setImageCaptionText("Dummy caption");
		post.setImageCreditsText("Dummy credits");
		post.setImageTitleText("Dummy title");
		return post;
	}

	public static Image createImage(Image.Type type) {
		Image image = new Image(type);
		for (String s : image.getSizeTags()) {
			image.addPicture(createPicture(s));
		}
		image.setOriginalHash(generateToken());

		return image;
	}

	public static Picture createPicture(String sizeTag) {
		Picture p1 = new Picture();
		p1.setSizeTag(sizeTag);
		File f1 = new File();
		f1.setHash(generateToken());
		p1.setFile(f1);
		return p1;
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
		person.setTenantId(TENANT);
		person.id = 10;
		person.bookmarkPosts = Lists.newArrayList(10, 11, 12);
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
		station.setTenantId(TENANT);
		station.name = "TUPY";
		station.visibility = "UNRESTRICTED";
		station.logo = createImage(Image.Type.LOGIN);
		station.defaultPerspectiveId = 7;
		station.categoriesTaxonomyId = 140;
		return station;
	}

	public static Page createPage() {
		Page page = new Page();
		page.setTenantId(TENANT);
		page.setTitle("Home");

		QueryableListSection section1 = createQueryableListSection();

		PostStatement postStatement2 = new PostStatement();
		postStatement2.setStations(Lists.newArrayList(11));
		postStatement2.setQuery("fhc");
		FixedQuery fixedQuery2 = new FixedQuery(postStatement2, Lists.newArrayList(0, 1, 2, 3, 4));
		QueryableListSection section2 = new QueryableListSection(5, Lists.newArrayList(fixedQuery2));
		section2.setTitle("Section 2");
		section2.setPageable(false);

		page.setSections(new TreeMap<Integer, AbstractSection>() {{
			put(0, section1);
			put(1, section2);
		}});

		return page;
	}

	public static QueryableListSection createQueryableListSection() {
		PostStatement postStatement1 = new PostStatement();
		postStatement1.setStations(Lists.newArrayList(11));
		postStatement1.setQuery("dilma");

		PageableQuery pageableQuery = new PageableQuery(postStatement1);

		FixedQuery fixedQuery1 = new FixedQuery(postStatement1, Lists.newArrayList(0, 2, 3));
		QueryableListSection section1 = new QueryableListSection(10, pageableQuery);
		section1.setTitle("Section 1");
		section1.setPageable(true);
		section1.setFixedQueries(Lists.newArrayList(fixedQuery1));

		Map<Integer, Block> blocks = new HashMap<>();
		blocks.put(0, new BlockImpl<>(() -> 2, Integer.class));

		try {
			setPrivateField(section1.getClass().getField("blocks"), section1, blocks);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return section1;
	}

	static void setPrivateField(Field field, Object obj, Object newValue) throws Exception {
		field.setAccessible(true);
		field.set(obj, newValue);
	}

	public static NotificationView createNotification() {
		Post post = createPost();
		NotificationView notification = new NotificationView(post.title, post.title, "ab157cb227", false);
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.networkId = 5;
		notification.post = new PostConverter(null, null).convertTo(post);
		notification.postId = post.id;
		notification.postTitle = post.title;
		notification.postSnippet = StringUtil.simpleSnippet(post.body);
		return notification;
	}
}
