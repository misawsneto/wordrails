package co.xarx.trix.test;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.domain.ESStation;
import co.xarx.trix.elasticsearch.mapper.PostMap;
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
}
