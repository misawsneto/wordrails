package co.xarx.trix.test;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.elasticsearch.domain.ESBookmark;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.domain.ESStation;
import co.xarx.trix.elasticsearch.mapper.PersonMap;
import co.xarx.trix.elasticsearch.mapper.PostMap;
import co.xarx.trix.elasticsearch.mapper.PostViewMap;
import co.xarx.trix.elasticsearch.mapper.StationMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ModelMapperTest {

	private ModelMapper modelMapper;

	@Before
	public void setUp() throws Exception {
		modelMapper = new ModelMapper();
		modelMapper.addMappings(new PostMap());
		modelMapper.addMappings(new StationMap());
		modelMapper.addMappings(new PersonMap());
		modelMapper.addMappings(new PostViewMap());
	}

	@Test
	public void testPostToESPostMapping() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		Post post = TestArtifactsFactory.createPost();
		ESPost esPost = modelMapper.map(post, ESPost.class);
		System.out.println(objectMapper.writeValueAsString(esPost));

		assertEquals(post.tags, esPost.tags);
		assertEquals(post.station.id, esPost.stationId);
	}

	@Test
	public void testStationToESPostMapping() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		Station station = TestArtifactsFactory.createStation();
		ESStation esStation = modelMapper.map(station, ESStation.class);
		System.out.println(objectMapper.writeValueAsString(esStation));

		assertEquals(station.id, esStation.id);
		assertEquals(station.logo.hashs, esStation.logo);
	}

	@Test
	public void testPersonToESPostMapping() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		Person person = TestArtifactsFactory.createPerson();
		ESPerson esPerson = modelMapper.map(person, ESPerson.class);
		System.out.println(objectMapper.writeValueAsString(esPerson));

		assertEquals(person.id, esPerson.id);
		assertEquals(person.cover.hashs, esPerson.cover);
	}

	@Test
	public void testBookmarkToESBookmarkMapping() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		Bookmark bookmark = TestArtifactsFactory.createBookmark();
		ESBookmark esBookmark = modelMapper.map(bookmark, ESBookmark.class);
		System.out.println(objectMapper.writeValueAsString(esBookmark));

		assertEquals(bookmark.id, esBookmark.id);
		assertEquals(bookmark.person.id, esBookmark.personId);
		assertEquals(bookmark.post.id, esBookmark.postId);
	}

	@Test
	public void testESPostToPostViewMapping() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		Post post = TestArtifactsFactory.createPost();
		ESPost esPost = modelMapper.map(post, ESPost.class);
		System.out.println(objectMapper.writeValueAsString(esPost));

		esPost.author = TestArtifactsFactory.createPerson();
		PostView postView = modelMapper.map(esPost, PostView.class);
		System.out.println(objectMapper.writeValueAsString(postView));

		assertEquals(postView.tags, esPost.tags);
		assertEquals(postView.stationId, esPost.stationId);
	}
}
