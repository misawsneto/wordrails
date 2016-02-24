package co.xarx.trix.web.rest;

import co.xarx.trix.domain.File;
import co.xarx.trix.domain.FileContents;
import co.xarx.trix.domain.QFile;
import co.xarx.trix.domain.QFileContents;
import co.xarx.trix.persistence.FileContentsRepository;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.jboss.resteasy.annotations.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Path("/files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class FilesResource {

	private static final Integer MAX_SIZE = 6291456;

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private FileContentsRepository fileContentsRepository;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@PersistenceContext
	private EntityManager manager;


	@Deprecated
	@PUT
	@Path("{id}/contents")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response putFileContents(@PathParam("id") Integer id, @Context HttpServletRequest request) throws FileUploadException, IOException {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		}

		String hash = FileUtil.getHash(item.getInputStream());
		File existingFile = fileRepository.findOne(QFile.file.hash.eq(hash));
		if (existingFile != null) {
			return getResponseFromId(existingFile.id);
		}

		try {
			if (validate(item)) {
				FileContents file = fileContentsRepository.findOne(id);

				if (file == null) {
					return Response.status(Status.NOT_FOUND).build();
				}

				Session session = (Session) manager.getDelegate();
				LobCreator creator = Hibernate.getLobCreator(session);

				file.hash = hash;
				file.type = File.INTERNAL;
				file.directory = File.DIR_IMAGES;
				file.mime = item.getContentType();
				file.contents = creator.createBlob(item.getInputStream(), item.getSize());
				file.size = item.getSize();
				fileContentsRepository.save(file);

				return getResponseFromId(id);
			}
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximum size is 6MB\", maxMb:6}").build();
		}

		return Response.status(Status.BAD_REQUEST).build();
	}

	private Response getResponseFromId(Integer id) {
		URI location = UriBuilder.fromResource(FilesResource.class).path(id.toString()).path("contents").build();

		return Response.ok().entity("{\"filelink\":\"/api" + location + "\", " +
				"\"link\":\"/api" + location + "\", " +
				"\"id\":" + id + "}").build();
	}


	@POST
	@Path("/contents/simple")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postSimpleFileContents(@Context HttpServletRequest request) throws FileUploadException, IOException {
		try {
			FileItem item = FileUtil.getFileFromRequest(request);

			if (!validate(item)) {
				return Response.status(Status.BAD_REQUEST).build();
			}

			if (item == null) {
				return Response.noContent().build();
			}

			Session session = (Session) manager.getDelegate();
			LobCreator creator = Hibernate.getLobCreator(session);

			String hash = FileUtil.getHash(item.getInputStream());
//			FileContents existingFile = fileContentsRepository.findOne(QFileContents.fileContents.hash.eq(hash));
			FileContents existingFile = null;
			Iterable<FileContents> all = fileContentsRepository.findAll(QFileContents.fileContents.hash.eq(hash));
			if(all != null && all.iterator().hasNext())
				existingFile = all.iterator().next();
			if (existingFile != null) {
				if (existingFile.type.equals(File.EXTERNAL)) {
					if (!amazonCloudService.exists(AmazonCloudService.IMAGE_DIR, existingFile.hash)) {
						existingFile.size = item.getSize();
						existingFile.type = File.INTERNAL;
						existingFile.contents = creator.createBlob(item.getInputStream(), item.getSize());
						fileContentsRepository.save(existingFile);
					}
				}

				return getResponseFromId(existingFile.id);
			} else {
				FileContents file = new FileContents();
				file.hash = hash;
				file.type = File.INTERNAL;
				file.directory = File.DIR_IMAGES;
				file.mime = item.getContentType();
				file.contents = creator.createBlob(item.getInputStream(), item.getSize());
				file.size = item.getSize();
				fileContentsRepository.save(file);

				return getResponseFromId(file.id);

			}
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximum size is 6MB\", maxMb:6}").build();
		}
	}

	private boolean validate(FileItem item) throws FileUploadException {
		if (item.getFieldName().equals("contents") || item.getFieldName().equals("file")) {
			if (item.getSize() > MAX_SIZE) {
				throw new FileUploadException("Maximum file size is 6MB");
			}

			return true;
		}
		return false;
	}

	@GET
	@Path("{id}/contents")
	@Cache(isPrivate = false, maxAge = 31536000)
	public Response getFileContents(@PathParam("id") Integer id, @Context HttpServletResponse response) throws SQLException, IOException {
		String hash = fileRepository.findExternalHashById(id);

		FileContents file = null;
		if (hash == null || hash.isEmpty()) {
			file = fileContentsRepository.findOne(id);
			if (file == null || file.contents == null) {
				return Response.status(Status.NO_CONTENT).build();
			}
		}

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
		} else if (file != null && file.contents != null) {
			return Response.ok(file.contents.getBinaryStream(), file.mime).build();
		}

		return Response.status(Status.NO_CONTENT).build();
	}


}