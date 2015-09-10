package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.business.GlobalParameter;
import com.wordrails.business.Network;
import com.wordrails.business.TrixFile;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.GlobalParameterRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import java.io.File;
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
			FileItem item = getFileFromRequest(request);
			trixFile.hash = sendFile(network.domain, item);

			fileRepository.save(trixFile);

			URI location = UriBuilder.fromResource(FilesResource.class).path(id.toString()).path("contents").build();
			return Response.status(Status.NO_CONTENT).location(location).build();
		} catch (BadRequestException ue) {
			return Response.status(Status.BAD_REQUEST).build();
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximum size is 6MB\", maxMb:6}").build();
		}
	}

	private FileItem getFileFromRequest(HttpServletRequest request) throws FileUploadException {
		ServletContext context = request.getServletContext();
		File repository = (File) context.getAttribute(ServletContext.TEMPDIR);
		DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		if (items != null && !items.isEmpty()) {
			return items.get(0);
		}

		return null;
	}


	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response upload(@Context HttpServletRequest request) throws FileUploadException, IOException {
		try {
			FileItem item = getFileFromRequest(request);

			if (item == null) {
				return Response.noContent().build();
			}

			Network network = wordrailsService.getNetworkFromHost(request);
			String trixHash = sendFile(network.domain, item);
			TrixFile file = new TrixFile(trixHash);
			fileRepository.save(file);

			return Response.ok().entity("{\"hash\":" + trixHash + "}").build();
		} catch (BadRequestException ue) {
			return Response.status(Status.BAD_REQUEST).build();
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximum size is 6MB\", maxMb:6}").build();
		}
	}


	@POST
	@Path("/contents/simple")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postSimpleFileContents(@Context HttpServletRequest request) throws FileUploadException, IOException {
		try {
			FileItem item = getFileFromRequest(request);

			if (item == null) {
				return Response.noContent().build();
			}

			Network network = wordrailsService.getNetworkFromHost(request);
			String trixHash = sendFile(network.domain, item);
			TrixFile file = new TrixFile(trixHash);
			fileRepository.save(file);

			URI location = UriBuilder.fromResource(FilesResource.class).path(String.valueOf(file.id)).path("contents").build();

			return Response.ok().entity("{\"filelink\":\"/api" + location + "\", " +
					"\"link\":\"/api" + location + "\", " +
					"\"id\":" + file.id + "}").build();
		} catch (BadRequestException ue) {
			return Response.status(Status.BAD_REQUEST).build();
		} catch (FileUploadException ue) {
			return Response.status(Status.REQUEST_ENTITY_TOO_LARGE).entity("{\"message\":\"TrixFile's maximum size is 6MB\", maxMb:6}").build();
		}
	}

	private String sendFile(String domain, FileItem item) throws FileUploadException {
		if (validate(item)) {
			try {
				InputStream input = item.getInputStream();
				return fileService.newFile(input, domain, item.getContentType(), item.getSize());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		throw new BadRequestException();
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
	public Response getFileContents(@PathParam("id") Integer id, @Context HttpServletResponse response, @Context HttpServletRequest request) throws SQLException, IOException {
		Network network = wordrailsService.getNetworkFromHost(request);

		if(network == null) {
			return Response.serverError().entity("network of the request is null").build();
		} else if(network.domain == null) {
			return Response.serverError().entity("subdomain of network is null").build();
		}

		String hash = fileRepository.findHashById(id);
		if(hash == null)
			return Response.status(Status.BAD_REQUEST).entity("file doesnt exist").build();
		if(hash.isEmpty())
			return Response.status(Status.BAD_REQUEST).entity("file wasnt uploaded properly").build();

		response.sendRedirect(amazonCloudService.getURL(network.domain, hash));
		return Response.ok().build();
	}
}