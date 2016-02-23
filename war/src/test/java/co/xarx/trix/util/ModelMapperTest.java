package co.xarx.trix.util;

import co.xarx.trix.TestArtifactsFactory;
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

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ModelMapperTest {

	private ModelMapper modelMapper;

	@Before
	public void setUp() throws Exception {
		modelMapper = new ModelMapper();
		modelMapper.addMappings(new PersonMap());
		modelMapper.addMappings(new PostMap());
		modelMapper.addMappings(new StationMap());
	}

	@Test
	public void testPostToESPostMapping() throws Exception {
		Post post = TestArtifactsFactory.createPost();
		ESPost esPost = modelMapper.map(post, ESPost.class);

		assertEquals(post.tags, esPost.tags);
		assertEquals(post.station.id, esPost.stationId);
	}

	@Test
	public void testStationToESStationMapping() throws Exception {
		Station station = TestArtifactsFactory.createStation();
		ESStation esStation = modelMapper.map(station, ESStation.class);

		assertEquals(station.id, esStation.id);
		assertEquals(station.logo.hashs, esStation.logo);
	}

	@Test
	public void testPersonToESPersonMapping() throws Exception {
		Person person = TestArtifactsFactory.createPerson();
		ESPerson esPerson = modelMapper.map(person, ESPerson.class);

		assertEquals(person.id, esPerson.id);
		assertEquals(person.cover.getOriginalHash(), esPerson.cover);
	}

	@After
	public void after() {
		modelMapper.validate();
	}
}
