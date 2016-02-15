package co.xarx.trix.util;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.domain.ESStation;
import co.xarx.trix.elasticsearch.mapper.PersonMap;
import co.xarx.trix.elasticsearch.mapper.PostMap;
import co.xarx.trix.elasticsearch.mapper.StationMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class ModelMapperTest {

	private ModelMapper modelMapper;

	@Before
	public void setUp() throws Exception {
		modelMapper = new ModelMapper();
		modelMapper.addMappings(new PostMap());
		modelMapper.addMappings(new StationMap());
		modelMapper.addMappings(new PersonMap());
//		modelMapper.addMappings(new PostViewMap());
	}

	@Test
	public void testPostToESPostMapping() throws Exception {
		Post post = TestArtifactsFactory.createPost();
		modelMapper.map(post, ESPost.class);
	}

	@Test
	public void testStationToESStationMapping() throws Exception {
		Station station = TestArtifactsFactory.createStation();
		modelMapper.map(station, ESStation.class);
	}

	@Test
	public void testPersonToESPersonMapping() throws Exception {
		Person person = TestArtifactsFactory.createPerson();
		modelMapper.map(person, ESPerson.class);
	}

//	@Test
	public void testESPostToPostViewMapping() throws Exception {
		Post post = TestArtifactsFactory.createPost();
		ESPost esPost = modelMapper.map(post, ESPost.class);

		Person person = TestArtifactsFactory.createPerson();
		esPost.author = modelMapper.map(person, ESPerson.class);
		modelMapper.map(esPost, PostView.class);
	}

	@After
	public void after() {
		modelMapper.validate();
	}
}
