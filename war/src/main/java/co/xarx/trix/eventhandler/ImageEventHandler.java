package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Image;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(Image.class)
@Component
public class ImageEventHandler {

//	@Autowired
//	private TrixAuthenticationProvider authProvider;
//	@Autowired
//	private AmazonCloudService amazonCloudService;
//	@Autowired
//	private FileContentsRepository fileContentsRepository;
//
//	@Autowired
//	private ImageService imageService;
//
//	@HandleBeforeCreate
//	public void handleBeforeCreate(Image image) throws Exception {
//		if(image.pictures != null && !image.pictures.isEmpty()) return;
//
//		if (image.type == null || image.type.trim().isEmpty())
//			image.type = Image.Type.POST.toString();
//
//		if (image.original == null) {
//			throw new NullPointerException("original icon can't be null");
//		}
//
//		Image newImage;
//		FileContents originalFile = fileContentsRepository.findOne(image.original.id);
//		//if is external, is already uploaded to amazon, so the file was uploaded before
//		if (originalFile.type.equals(File.EXTERNAL) && amazonCloudService.exists(AmazonCloudService.IMAGE_DIR, originalFile.hash)) {
//			Image existingImage = imageService.getImageFromHashAndType(image.type, originalFile.hash);
//
//			java.io.File tempOriginalFile =
//					FileUtil.downloadFile(FileUtil.createNewTempFile(), amazonCloudService.getPublicImageURL(originalFile.hash));
//
//			newImage = imageService.createNewImageFromExistingImage(false, existingImage, tempOriginalFile);
//		} else if (originalFile.type.equals(File.INTERNAL)) {
//			//if it's running here, this is a new image and needs to upload all sizes
//			try (InputStream inputStream = originalFile.contents.getBinaryStream()) {
//				newImage = imageService.createNewImage(image, inputStream, originalFile.mime, false, true);
//				originalFile.contents = null;
//				originalFile.type = File.EXTERNAL;
//			} catch (Exception e) {
//                Logger.error(e.getMessage(), e);
//				throw new Exception("something bad happened uploading new image", e);
//			}
//		} else {
//			throw new BadRequestException("Image is stored in database but doesn't exist on amazon servers. Upload the image again");
//		}
//
//		image.pictures = newImage.pictures;
//		image.vertical = newImage.vertical;
//		image.original = newImage.original;
//		image.large = newImage.large;
//		image.medium = newImage.medium;
//		image.small = newImage.small;
//	}
}