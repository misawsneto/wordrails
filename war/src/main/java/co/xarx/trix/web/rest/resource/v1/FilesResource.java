package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.domain.Audio;
import co.xarx.trix.domain.Document;
import co.xarx.trix.domain.Video;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.FileService;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.FilesApi;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@NoArgsConstructor
public class FilesResource extends AbstractResource implements FilesApi {

	private FileRepository fileRepository;
	private AmazonCloudService amazonCloudService;
	private FileService fileService;

	@Autowired
	@Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	@Autowired
	public FilesResource(FileRepository fileRepository, AmazonCloudService amazonCloudService, FileService fileService) {
		this.fileRepository = fileRepository;
		this.amazonCloudService = amazonCloudService;
		this.fileService = fileService;
	}

	private static class FileUpload implements Serializable{
		private static final long serialVersionUID = 1474032587285767669L;
		public String hash;
		public Integer id;
		public String link;
		public String filelink;
	}

	@Deprecated
	@Override
	public Response getFileContents(Integer id) throws SQLException, IOException {
		String hash = fileRepository.findExternalHashById(id);

		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=2592000");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 30);
		//HTTP header date format: Thu, 01 Dec 1994 16:00:00 GMT
		String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
		response.setHeader("Expires", o);

		if (hash != null && !hash.isEmpty()) {
			response.sendRedirect(amazonCloudService.getPublicImageURL(hash));
			return Response.ok().build();
		}

		return Response.status(Status.NO_CONTENT).build();
	}

	@Override
	public Response uploadVideo() throws Exception {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		} else if (!fileService.validate(item, FileService.MAX_SIZE_32)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}


		File originalFile = FileUtil.createNewTempFile(item.getInputStream());

		Video newVideo = fileService.createAndSaveNewVideo(item.getName(), originalFile, item
				.getContentType());

		if (originalFile.exists())
			originalFile.delete();

		FileUpload fileUpload = new FileUpload();
		fileUpload.hash = FileUtil.getHash(item.getInputStream());
		fileUpload.id = newVideo.getId();
		fileUpload.link = amazonCloudService.getPublicFileURL(fileUpload.hash, co.xarx.trix.domain.File.DIR_VIDEO);
		fileUpload.filelink = fileUpload.link;

		return Response.ok().entity(simpleMapper.writeValueAsString(fileUpload)).build();
	}

	@Override
	public Response uploadAudio() throws Exception {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		} else if (!fileService.validate(item, FileService.MAX_SIZE_32)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}


		File originalFile = FileUtil.createNewTempFile(item.getInputStream());

		Audio newAudio = fileService.createAndSaveNewAudio(item.getName(), originalFile, item
				.getContentType());

		if (originalFile.exists())
			originalFile.delete();

		FileUpload fileUpload = new FileUpload();
		fileUpload.hash = FileUtil.getHash(item.getInputStream());
		fileUpload.id = newAudio.getId();
		fileUpload.link = amazonCloudService.getPublicFileURL(fileUpload.hash, co.xarx.trix.domain.File.DIR_AUDIO);
		fileUpload.filelink = fileUpload.link;

		return Response.ok().entity(simpleMapper.writeValueAsString(fileUpload)).build();
	}

	@Override
	public Response uploadDoc() throws Exception {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		} else if (!fileService.validate(item, FileService.MAX_SIZE_16)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}


		File originalFile = FileUtil.createNewTempFile(item.getInputStream());

		Document newDoc = fileService.createAndSaveNewDoc(item.getName(), originalFile, item
				.getContentType());

		if (originalFile.exists())
			originalFile.delete();

		FileUpload fileUpload = new FileUpload();
		fileUpload.hash = FileUtil.getHash(item.getInputStream());
		fileUpload.id = newDoc.getId();
		fileUpload.link = amazonCloudService.getPublicFileURL(fileUpload.hash, co.xarx.trix.domain.File.DIR_DOC);
		fileUpload.filelink = fileUpload.link;

		return Response.ok().entity(simpleMapper.writeValueAsString(fileUpload)).build();
	}

	@Override
	public Response getFile(String hash, String type) throws IOException {

		if(StringUtils.isEmpty(hash))
			return Response.status(Response.Status.NO_CONTENT).build();

		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=2592000");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 30);
		String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
		response.setHeader("Expires", o);

		response.sendRedirect(amazonCloudService.getPublicFileURL(hash, type));
		return Response.ok().build();
	}
}