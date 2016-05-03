package co.xarx.trix.util;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.v2.ImageData;
import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.api.v2.PictureData;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.config.modelmapper.*;
import co.xarx.trix.domain.*;
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
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ModelMapperTest {

	private ModelMapper modelMapper;

	@Before
	public void setUp() throws Exception {
		modelMapper = new ModelMapper();

		modelMapper.addMappings(new PersonMap());
		modelMapper.addMappings(new PostMap());
		modelMapper.addMappings(new StationMap());
		modelMapper.addMappings(new CategoryDataMap());
		modelMapper.addMappings(new PictureDataMap());
		modelMapper.addMappings(new ImageDataMap());
		modelMapper.addMappings(new PostImageDataMap());
		modelMapper.addMappings(new PersonDataMap());
		modelMapper.addMappings(new PostDataMap());
		modelMapper.addMappings(new PageDataMap());
	}

	@Test
	public void testPostToESPostMapping() throws Exception {
		Post post = TestArtifactsFactory.createPost();
		ESPost esPost = modelMapper.map(post, ESPost.class);

		assertEquals(post.tags, esPost.tags);
		assertEquals(post.station.id, esPost.stationId);
	}

	@Test
	public void testPostToPostDataMapping() throws Exception {
		Post post = TestArtifactsFactory.createPost();
		PostData data = modelMapper.map(post, PostData.class);

		assertEquals(post.getTags(), data.getTags());
		assertNotNull(data.getVideo());
		assertNotNull(data.getAuthor());
		assertNotNull(data.getAudio());
		assertNotNull(data.getImage());
		assertNotNull(data.getImageHash());
		assertNotNull(data.getImage().getPictures());
		assertNotNull(data.getImage().getCaption());
		assertNotNull(data.getImage().getCredits());
		assertNotNull(data.getImage().getTitle());
		assertNotNull(data.getImage().isLandscape());
	}

	@Test
	public void testStationToESStationMapping() throws Exception {
		Station station = TestArtifactsFactory.createStation();
		ESStation esStation = modelMapper.map(station, ESStation.class);

		assertEquals(station.id, esStation.id);
		assertEquals(station.logo.getHashs(), esStation.logo);
	}

	@Test
	public void testPersonToESPersonMapping() throws Exception {
		Person person = TestArtifactsFactory.createPerson();
		ESPerson esPerson = modelMapper.map(person, ESPerson.class);

		assertEquals(person.id, esPerson.id);
		assertEquals(person.cover.getOriginalHash(), esPerson.cover);
	}

	@Test
	public void testPersonToPersonDataMapping() throws Exception {
		Person person = TestArtifactsFactory.createPerson();
		PersonData personData = modelMapper.map(person, PersonData.class);

		assertEquals(person.id, personData.getId());
		assertEquals(person.getEmail(), personData.getEmail());
	}

	@Test
	public void testImageToImageDataMapping() throws Exception {
		Image image = TestArtifactsFactory.createImage(Image.Type.COVER);
		ImageData data = modelMapper.map(image, ImageData.class);

		assertEquals(image.getPictures().size(), data.getPictures().size());
	}

	@Test
	public void testPictureToPictureDataMapping() throws Exception {
		Picture pic = TestArtifactsFactory.createPicture("original");
		PictureData data = modelMapper.map(pic, PictureData.class);

		assertEquals(pic.getFile().getHash(), data.getHash());
	}

	@After
	public void after() {
		modelMapper.validate();
	}
}
