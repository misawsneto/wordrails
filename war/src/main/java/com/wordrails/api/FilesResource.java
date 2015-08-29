package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.business.Network;
import com.wordrails.business.TrixFile;
import com.wordrails.persistence.FileRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tika.Tika;
import org.jboss.resteasy.annotations.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Path("/files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class FilesResource {

	private static final Integer MAX_SIZE = 6291456;

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private FileService fileService;


	@Deprecated
	@PUT
	@Path("{id}/contents")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response putFileContents(@PathParam("id") Integer id, @Context HttpServletRequest request) throws FileUploadException, IOException {
		Network network = wordrailsService.getNetworkFromHost(request);
		TrixFile trixFile = fileRepository.findOne(id);

		if (trixFile == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			TrixFile newTrixFile = sendFile(network.domain, request);

			trixFile.type = TrixFile.EXTERNAL_FILE;
			trixFile.mime = newTrixFile.mime;
			trixFile.name = newTrixFile.name;
			fileRepository.save(trixFile);

			URI location = UriBuilder.fromResource(FilesResource.class).path(id.toString()).path("contents").build();
			return Response.status(Status.NO_CONTENT).location(location).build();
		} catch (BadRequestException ue) {
			return Response.status(Status.BAD_REQUEST).build();
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximun size is 6MB\", maxMb:6}").build();
		}
	}

	private List<FileItem> getFilesFromRequest(HttpServletRequest request) throws FileUploadException {
		ServletContext context = request.getServletContext();
		java.io.File repository = (java.io.File) context.getAttribute(ServletContext.TEMPDIR);
		DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		return upload.parseRequest(request);
	}


	@PUT
	@Path("{id}/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@PathParam("id") Integer id, @Context HttpServletResponse response, @Context HttpServletRequest request) throws FileUploadException, IOException {
		Network network = wordrailsService.getNetworkFromHost(request);
		TrixFile trixFile = fileRepository.findOne(id);

		if (trixFile == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			TrixFile newTrixFile = sendFile(network.domain, request);

			trixFile.type = TrixFile.EXTERNAL_FILE;
			trixFile.mime = newTrixFile.mime;
			trixFile.name = newTrixFile.name;
			fileRepository.save(trixFile);

			response.sendRedirect(amazonCloudService.getURL(network.domain, trixFile.id + ""));
			return Response.ok().build();
		} catch (BadRequestException ue) {
			return Response.status(Status.BAD_REQUEST).build();
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximun size is 6MB\", maxMb:6}").build();
		}
	}


	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@Context HttpServletResponse response, @Context HttpServletRequest request) throws FileUploadException, IOException {
		try {
			Network network = wordrailsService.getNetworkFromHost(request);
			TrixFile trixFile = sendFile(network.domain, request);

			String url = amazonCloudService.getURL(network.domain, trixFile.id + "");
			return Response.ok().entity("{\"url\":\"" + url + "\"").build();
		} catch (BadRequestException ue) {
			return Response.status(Status.BAD_REQUEST).build();
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximun size is 6MB\", maxMb:6}").build();
		}
	}


	@Deprecated
	@POST
	@Path("/contents/simple")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postSimpleFileContents(@Context HttpServletRequest request) throws FileUploadException, IOException {
		try {
			Network network = wordrailsService.getNetworkFromHost(request);
			TrixFile trixFile = sendFile(network.domain, request);
			URI location = UriBuilder.fromResource(FilesResource.class).path(trixFile.id + "").path("contents").build();

			return Response.ok().entity("{\"filelink\":\"/api" + location + "\", " +
							"\"link\":\"/api" + location + "\", " +
							"\"id\":" + trixFile.id + "}").build();
		} catch (BadRequestException ue) {
			return Response.status(Status.BAD_REQUEST).build();
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximun size is 6MB\", maxMb:6}").build();
		}
	}

	private TrixFile sendFile(String domain, HttpServletRequest request) throws FileUploadException {
		List<FileItem> items = getFilesFromRequest(request);

		for (FileItem item : items) {
			if (item.getFieldName().equals("contents") || item.getFieldName().equals("file")) {
				if (item.getSize() > MAX_SIZE) {
					throw new FileUploadException("Maximum file size is 6MB");
				}

				try {
					InputStream input = item.getInputStream();
					return fileService.newFile(input, domain);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		throw new BadRequestException();
	}

	@GET
	@Path("{id}/contents")
	@Cache(isPrivate = false, maxAge = 31536000)
	public Response getFileContents(@PathParam("id") Integer id, @Context HttpServletResponse response, @Context HttpServletRequest request) throws SQLException, IOException {
		Network network = wordrailsService.getNetworkFromHost(request);
		response.sendRedirect(amazonCloudService.getURL(network.domain, id + ""));
		return Response.ok().build();
	}
}