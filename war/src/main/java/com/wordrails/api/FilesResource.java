package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.business.Network;
import com.wordrails.business.File;
import com.wordrails.persistence.FileRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
		String subdomain = getSubdomain(request.getHeader("Host"));
		if(subdomain == null || subdomain.isEmpty()){
			return Response.serverError().entity("subdomain of network is null").build();
		}
		File file = fileRepository.findOne(id);

		if (file == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			FileItem item = getFileFromRequest(request);
			file.hash = sendFile(subdomain, item);

			fileRepository.save(file);

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
		java.io.File repository = (java.io.File) context.getAttribute(ServletContext.TEMPDIR);
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

			String subdomain = getSubdomain(request.getHeader("Host"));
			if(subdomain == null || subdomain.isEmpty()){
				return Response.serverError().entity("subdomain of network is null").build();
			}
			String trixHash = sendFile(subdomain, item);
			File file = new File(trixHash);
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

			String subdomain = getSubdomain(request.getHeader("Host"));
			if(subdomain == null || subdomain.isEmpty()){
				return Response.serverError().entity("subdomain of network is null").build();
			}
			String trixHash = sendFile(subdomain, item);
			File file = new File(trixHash);
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

	private String getSubdomain(String host) {
		String subdomain = wordrailsService.getSubdomainFromHost(host);
		if(subdomain == null || subdomain.isEmpty()){
			Network network = wordrailsService.getNetworkFromHost(host);

			if(network != null) {
				return network.subdomain;
			}
		}

		return subdomain;
	}

	@GET
	@Path("{id}/contents")
	public Response getFileContents(@PathParam("id") Integer id, @Context HttpServletResponse response, @Context HttpServletRequest request) throws SQLException, IOException {
		String subdomain = getSubdomain(request.getHeader("Host"));
		if(subdomain == null || subdomain.isEmpty()){
			return Response.serverError().entity("subdomain of network is null").build();
		}

		String hash = fileRepository.findHashById(id);
		if(hash == null)
			return Response.status(Status.NOT_FOUND).entity("file doesnt exist").build();
		if(hash.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("file wasnt uploaded properly").build();

		response.sendRedirect(amazonCloudService.getPublicImageURL(subdomain, hash));
		return Response.ok().build();
	}
}