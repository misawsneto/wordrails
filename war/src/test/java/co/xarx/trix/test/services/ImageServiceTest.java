package co.xarx.trix.test.services;

import co.xarx.trix.domain.Image;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.test.AbstractIntegrationTest;
import co.xarx.trix.test.config.ApplicationTestConfig;
import co.xarx.trix.test.config.DatabaseTestConfig;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.ImageUtil;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PrepareForTest({FileUtil.class, ImageUtil.class})
@ContextConfiguration(
		loader = AnnotationConfigContextLoader.class,
		classes = {ApplicationTestConfig.class, DatabaseTestConfig.class}
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ImageServiceTest extends AbstractIntegrationTest {

	Logger logger = LoggerFactory.getLogger(ImageServiceTest.class);

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	@Rule
	public TestWatcher watchman = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			logger.info("Test {} is running.", description.getMethodName());
		}

		@Override
		protected void succeeded(Description description) {
			logger.info("Test {} succesfully run.", description.getMethodName());
		}

		@Override
		protected void failed(Throwable e, Description description) {
			logger.info("Test {} failed with {} reason.", description.getMethodName(), e.getMessage());
		}
	};

	private ImageService imageService;

	@Autowired
	private ImageRepository imageRepository;
	private AmazonCloudService amazonCloudService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private PictureRepository pictureRepository;

	@Before
	public void setUp() throws Exception {
		amazonCloudService = mock(AmazonCloudService.class);
		imageService = new ImageService(imageRepository, amazonCloudService, fileRepository, pictureRepository);

//		File fileMock = folder.newFile("image.jpg");

//		ImageUtil.ImageFile imageFile = new ImageUtil.ImageFile(fileMock, 0, 0, "hashoriginal");

//		PowerMockito.mockStatic(ImageUtil.class);
//		PowerMockito.when(ImageUtil.createNewImageTrixFile(anyString(), anyLong())).thenCallRealMethod();
//		PowerMockito.when(ImageUtil.getImageFile(any(File.class))).thenReturn(imageFile);
//
//		PowerMockito.mockStatic(FileUtil.class);
//		PowerMockito.when(FileUtil.createNewTempFile()).thenReturn(fileMock);
//		PowerMockito.when(FileUtil.createNewTempFile(any(InputStream.class))).thenReturn(fileMock);
	}

	@Test(expected = AmazonS3Exception.class)
	public void testAUploadImageFailed() throws Exception {
		Image newImage = new Image(Image.Type.PROFILE_PICTURE);
		newImage.title = "fake title 2";

		File fileMock = new File(getClass().getResource("/image.jpg").getFile());
		when(amazonCloudService.uploadPublicImage(any(File.class), anyLong(), anyString(), anyString(), anyString(), anyBoolean())).thenAnswer(i -> FileUtil.getHash(new FileInputStream(i.getArgumentAt(0, File.class)))).thenThrow(AmazonS3Exception.class);

		imageService.createNewImage(newImage, new FileInputStream(fileMock), "image/jpg");
	}

	@Test
	public void testBUploadImage() throws Exception {
		Image newImage = new Image(Image.Type.POST);
		newImage.title = "fake title";

//		File fileMock = PowerMockito.mock(File.class);
//		ImageUtil.ImageFile imageFile = new ImageUtil.ImageFile(fileMock, 0, 0, "hashoriginal");
//
//		PowerMockito.when(ImageUtil.resizeImage(any(File.class), anyObject(), anyObject(), anyObject()))
//				.thenReturn(imageFile);
//		PowerMockito.when(ImageUtil.resizeImage(any(File.class), anyObject(), anyObject(), anyObject(), anyObject()))
//				.thenReturn(imageFile);

		File fileMock = new File(getClass().getResource("/image.jpg").getFile());
		when(amazonCloudService.uploadPublicImage(any(File.class), anyLong(), anyString(), anyString(), anyString(), anyBoolean()))
				.thenAnswer(i -> FileUtil.getHash(new FileInputStream(i.getArgumentAt(0, File.class))));

		newImage = imageService.createNewImage(newImage, new FileInputStream(fileMock), "image/jpg");

		assertEquals(newImage.hashs.size(), newImage.getSizes().size()+1);

		Image duplicateImage = imageService.createNewImage(new Image(Image.Type.PROFILE_PICTURE), new FileInputStream(fileMock), "image/jpg");

		assertEquals(duplicateImage, newImage);
	}


}
