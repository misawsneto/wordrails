package co.xarx.trix.test.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Image;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.test.config.ApplicationTestConfig;
import co.xarx.trix.test.config.DatabaseTestConfig;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.ImageUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore({
		"javax.management.*",
		"javax.xml.parsers.*",
		"org.apache.log4j.*",
		"org.xml.sax.*",
		"org.w3c.dom.*",
		"com.sun.xml.*",
		"javax.xml.stream.*",
		"com.sun.org.apache.xerces.internal.jaxp.*",
		"ch.qos.logback.*",
		"org.slf4j.*"
})
@PrepareForTest({FileUtil.class, ImageUtil.class, TenantContextHolder.class})
@ActiveProfiles(profiles = "dev")
@Transactional
@ContextConfiguration(
		loader = AnnotationConfigContextLoader.class,
		classes = {ApplicationTestConfig.class, DatabaseTestConfig.class}
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ImageServiceTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private ImageService imageService;

	@Autowired
	private ImageRepository imageRepository;
	@Mock
	private AmazonCloudService amazonCloudService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private PictureRepository pictureRepository;

	@Before
	public void setUp() throws Exception {
		imageService = new ImageService(imageRepository, amazonCloudService, fileRepository, pictureRepository);

//		File fileMock = folder.newFile("image.jpg");

//		ImageUtil.ImageFile imageFile = new ImageUtil.ImageFile(fileMock, 0, 0, "hashoriginal");

		PowerMockito.mockStatic(TenantContextHolder.class);
		PowerMockito.when(TenantContextHolder.getCurrentTenantId()).thenReturn("test");

//		PowerMockito.mockStatic(ImageUtil.class);
//		PowerMockito.when(ImageUtil.createNewImageTrixFile(anyString(), anyLong())).thenCallRealMethod();
//		PowerMockito.when(ImageUtil.getImageFile(any(File.class))).thenReturn(imageFile);
//
//		PowerMockito.mockStatic(FileUtil.class);
//		PowerMockito.when(FileUtil.createNewTempFile()).thenReturn(fileMock);
//		PowerMockito.when(FileUtil.createNewTempFile(any(InputStream.class))).thenReturn(fileMock);
	}

	@Test
	public void testAUploadImage() throws Exception {
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

		newImage = imageService.createNewImage(newImage, new FileInputStream(fileMock), "image/jpg");
		Image duplicateImage = imageService.createNewImage(new Image(Image.Type.PROFILE_PICTURE),
				new FileInputStream(fileMock), "image/jpg");

		assertEquals(newImage.id, new Integer(1));
		assertEquals(newImage.hashs.size(), newImage.getSizes().size()+1);


		assertEquals(duplicateImage.id, new Integer(1));
		assertEquals(duplicateImage.hashs.size(), duplicateImage.getSizes().size() + 1);
	}

//	@Test
//	public void testBUploadDuplicateImage() throws Exception {
//		Image newImage = new Image(Image.Type.PROFILE_PICTURE);
//		newImage.title = "fake title 2";
//
//		File fileMock = new File(getClass().getResource("/image.jpg").getFile());
//
//		newImage = imageService.createNewImage(newImage, new FileInputStream(fileMock), "image/jpg");
//
//		assertEquals(newImage.id, new Integer(1));
//		assertEquals(newImage.hashs.size(), newImage.getSizes().size()+1);
//	}


}
