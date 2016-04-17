package co.xarx.trix.services;

import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import co.xarx.trix.persistence.*;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.ImageUtil;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.common.collect.Sets;
import com.mysema.query.types.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

	ImageService imageService;

	private ImageRepository imageRepository;
	private AmazonCloudServiceMock amazonCloudService;
	private FileRepository fileRepository;

	private File file;
	private String hash;

	private class AmazonCloudServiceMock extends AmazonCloudService {

		int countCall;

		public AmazonCloudServiceMock(AmazonS3Client s3Client) {
			super(s3Client);
		}

		@Override
		protected void uploadFile(File file, Long lenght,
								  String keyName, ObjectMetadata metadata,
								  boolean deleteFileAfterUpload) throws IOException, AmazonS3Exception {
			countCall++;
		}
	}

	@Before
	public void setUp() throws Exception {
		file = new java.io.File(getClass().getResource("/image.jpg").getFile());
		hash = FileUtil.getHash(new FileInputStream(file));

		imageRepository = mock(ImageRepository.class);
		amazonCloudService = new AmazonCloudServiceMock(null);
		fileRepository = mock(FileRepository.class);
		DocumentRepository dir = mock(DocumentRepository.class);
		VideoRepository vir = mock(VideoRepository.class);
		AudioRepository air = mock(AudioRepository.class);
		fileRepository = mock(FileRepository.class);

		ImageUtil imageUtil = new ImageUtil();
		FileService fs = new FileService(amazonCloudService, fileRepository, dir, air, vir);

		imageService = new ImageService(imageRepository, amazonCloudService, fileRepository, null, imageUtil, fs);
	}

	@Test
	public void testNewImageFromExisting() throws Exception {
		Image existingImage = new Image(Image.Type.POST);
		existingImage.setOriginalHash(hash);

		when(imageRepository.findOne(any(Predicate.class))).thenReturn(existingImage);

		Image image = imageService.createNewImage(Image.Type.POST.toString(), "anyname", file, "png");

		assertSame(existingImage, image);
	}

	private class SmallAndMediumImage extends Image {
		public SmallAndMediumImage() {
			Picture picSmall = new Picture("small", null);
			Picture picMedium = new Picture("medium", null);
			this.setPictures(Sets.newHashSet(picMedium, picSmall));
		}
	}

	@Test
	public void testNewImageFromExistingLackOneSize() throws Exception {
		Image existingImage = new SmallAndMediumImage();
		existingImage.setOriginalHash(hash);
		Set<Image.Size> existingSizes = new HashSet(existingImage.getSizes());

		when(imageRepository.findOne(any(Predicate.class))).thenReturn(existingImage);

		Image image = imageService.createNewImage(Image.Type.POST.toString(), "anyname", file, "png");

		assertSame(existingImage, image);
		assertNotSame(existingSizes, image.getSizes());
		assertTrue(existingSizes.size() < image.getSizes().size());
	}

	@Test
	public void testNewImageWithoutExisting() throws Exception {
		Image newImage = new Image(Image.Type.POST);
		newImage.setOriginalHash(hash);

		when(imageRepository.findOne(any(Predicate.class))).thenReturn(null);

		Image image = imageService.createNewImage(Image.Type.POST.toString(), "anyname", file, "png");

		assertNotSame(newImage, image);
		assertSame(newImage.getSizes(), image.getSizes());
		assertTrue(newImage.getSizes().size() == image.getSizes().size());
	}

	@Test
	public void testGetPicture() throws Exception {
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
		assertEquals(amazonCloudService.countCall, 0);
	}

	@Test
	public void testGetUploadPicture() throws Exception {
		when(fileRepository.findOne(any(Predicate.class))).thenReturn(null);

		Integer[] xy = new Integer[]{200, 200};

		Picture pic = imageService.getPictureByQuality(file, "medium", 500);

		Picture pic2 = imageService.getPictureBySize(file, "medium", xy);

		assertNotSame(pic.file.hash, hash);
		assertEquals(pic.sizeTag, "medium");

		assertEquals(amazonCloudService.countCall, 2);
		assertNotSame(pic2.file.hash, hash);
		assertEquals(pic2.sizeTag, "medium");
	}
}