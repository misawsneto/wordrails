package co.xarx.trix.services;

import co.xarx.trix.domain.Picture;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.util.FileUtil;
import com.mysema.query.types.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

	ImageService imageService;

	private ImageRepository imageRepository;
	private AmazonCloudService amazonCloudService;
	private FileRepository fileRepository;
	private PictureRepository pictureRepository;

	private File file;

	@Before
	public void setUp() throws Exception {
		file = new java.io.File(getClass().getResource("/image.jpg").getFile());

		imageRepository = mock(ImageRepository.class);
		amazonCloudService = mock(AmazonCloudService.class);
		fileRepository = mock(FileRepository.class);
		pictureRepository = mock(PictureRepository.class);

		imageService = new ImageService(imageRepository, amazonCloudService, fileRepository, pictureRepository);
	}

	@Test
	public void testGetPicture() throws Exception {
		String hash = FileUtil.getHash(new FileInputStream(file));

		co.xarx.trix.domain.File trixFile = new co.xarx.trix.domain.File();
		trixFile.id = 20;
		trixFile.hash = hash;
		when(fileRepository.findOne(any(Predicate.class))).thenReturn(trixFile);

		Integer[] xy = new Integer[]{200, 200};

		Picture pic = imageService.getPictureByQuality(file, "medium", 500);
		Picture pic2 = imageService.getPictureBySize(file, "medium", xy);

		assertEquals(pic.file, trixFile);
		assertNotSame(pic.file.hash, hash);
		assertEquals(pic.sizeTag, "medium");

		assertEquals(pic2.file, trixFile);
		assertNotSame(pic2.file.hash, hash);
		assertEquals(pic2.sizeTag, "medium");
		verify(amazonCloudService, times(0))
				.uploadPublicImage(any(File.class), anyLong(), anyString(), anyString(), anyString(), anyBoolean());
	}

	@Test
	public void testGetUploadPicture() throws Exception {
		String hash = FileUtil.getHash(new FileInputStream(file));

		when(fileRepository.findOne(any(Predicate.class))).thenReturn(null);

		Integer[] xy = new Integer[]{200, 200};

		Picture pic = imageService.getPictureByQuality(file, "medium", 500);

		Picture pic2 = imageService.getPictureBySize(file, "medium", xy);

		assertNotSame(pic.file.hash, hash);
		assertEquals(pic.sizeTag, "medium");

		verify(amazonCloudService, times(2))
				.uploadPublicImage(any(File.class), anyLong(), anyString(), anyString(), anyString(), anyBoolean());
		assertNotSame(pic2.file.hash, hash);
		assertEquals(pic2.sizeTag, "medium");
	}
}