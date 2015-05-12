package com.wordrails.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tika.Tika;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.jboss.resteasy.annotations.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.File;
import com.wordrails.business.FileContents;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;

@Path("/files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class FilesResource {
	private @PersistenceContext EntityManager manager;
	private @Autowired FileContentsRepository contentsRepository;
	private @Autowired FileRepository fileRepository;

	@PUT
	@Path("{id}/contents")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response putFileContents(@PathParam("id") Integer id, @Context HttpServletRequest request) throws FileUploadException, IOException {
		com.wordrails.business.File file = fileRepository.findOne(id);
		if (file == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			ServletContext context = request.getServletContext();		
			java.io.File repository = (java.io.File) context.getAttribute(ServletContext.TEMPDIR);		
			DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if ("contents".equals(item.getFieldName()) || "file".equals(item.getFieldName())) {
					if(item.getSize() <= 4194304){
						try (InputStream input = item.getInputStream()) {
							file.type = File.INTERNAL_FILE;
							file.mime = item.getContentType();
							file.mime = file.mime == null || file.mime.isEmpty() ? new Tika().detect(input) : file.mime;    
							file.name = item.getName();
							fileRepository.save(file);

							Session session = (Session) manager.getDelegate();
							LobCreator creator = Hibernate.getLobCreator(session);
							FileContents contents = contentsRepository.findOne(id);
							contents.contents = creator.createBlob(item.getInputStream(), item.getSize());
							contentsRepository.save(contents);

							URI location = UriBuilder.fromResource(FilesResource.class).path(id.toString()).path("contents").build();
							return Response.status(Status.NO_CONTENT).location(location).build();						
						}
					}else{
						return Response.status(Status.BAD_REQUEST).entity("File`s maximun size is 4194304 bytes").build();						
					}
				}
			}
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/contents/network")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postNetworkPostFileContents(@Context HttpServletRequest request) throws FileUploadException, IOException {
		com.wordrails.business.File file = new File();
		file.type = File.INTERNAL_FILE;
		fileRepository.save(file);
		Integer id = file.id;
		ServletContext context = request.getServletContext();		
		java.io.File repository = (java.io.File) context.getAttribute(ServletContext.TEMPDIR);		
		DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		for (FileItem item : items) {
			if ("contents".equals(item.getFieldName()) || "file".equals(item.getFieldName())) {
				if(item.getSize() <= 204800){
					try (InputStream input = item.getInputStream()) {
						file.type = File.INTERNAL_FILE;
						BufferedImage image = ImageIO.read(input);
						if(image.getHeight() > 100){
							return Response.status(Status.BAD_REQUEST).entity("{\"message\":\"Image height must be smaller than 100px.\"}").build();
						}
						file.mime = item.getContentType();
						file.mime = file.mime == null || file.mime.isEmpty() ? new Tika().detect(input) : file.mime;
						file.name = item.getName();
						fileRepository.save(file);

						Session session = (Session) manager.getDelegate();
						LobCreator creator = Hibernate.getLobCreator(session);
						FileContents contents = contentsRepository.findOne(id);
						contents.contents = creator.createBlob(item.getInputStream(), item.getSize());
						contentsRepository.save(contents);

						URI location = UriBuilder.fromResource(FilesResource.class).path(id.toString()).path("contents").build();
						return Response.status(Status.OK).entity("{\"filelink\":\"/api" +location+ "\", \"filelink\":\"/api" +location+ "\", \"id\":" + id + "}").build();					
					}
				}else{
					return Response.status(Status.BAD_REQUEST).entity("{\"message\":\"File`s maximun size is 200Kb.\"}").build();						
				}
			}
		}
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	@POST
	@Path("/contents/simple")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postPersonFileContents(@Context HttpServletRequest request) throws FileUploadException, IOException {
		com.wordrails.business.File file = new File();
		file.type = File.INTERNAL_FILE;
		fileRepository.save(file);
		Integer id = file.id;
		ServletContext context = request.getServletContext();		
		java.io.File repository = (java.io.File) context.getAttribute(ServletContext.TEMPDIR);		
		DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		for (FileItem item : items) {
			if ("contents".equals(item.getFieldName()) || "file".equals(item.getFieldName())) {
				if(item.getSize() <= 4194304){
					try (InputStream input = item.getInputStream()) {
						file.type = File.INTERNAL_FILE;
						file.mime = item.getContentType();
						file.mime = file.mime == null || file.mime.isEmpty() ? new Tika().detect(input) : file.mime;
						file.name = item.getName();
						fileRepository.save(file);

						Session session = (Session) manager.getDelegate();
						LobCreator creator = Hibernate.getLobCreator(session);
						FileContents contents = contentsRepository.findOne(id);
						contents.contents = creator.createBlob(item.getInputStream(), item.getSize());
						contentsRepository.save(contents);

						URI location = UriBuilder.fromResource(FilesResource.class).path(id.toString()).path("contents").build();
						return Response.status(Status.OK).entity("{\"filelink\":\"/api" +location+ "\", \"link\":\"/api" +location+ "\", \"id\":" + id + "}").build();					
					}
				}else{
					return Response.status(Status.BAD_REQUEST).entity("{\"message\":\"File`s maximun size is 4194304 bytes\"}").build();						
				}
			}
		}
		return Response.status(Status.BAD_REQUEST).build();
	}

	@GET
	@Path("{id}/contents")
	@Cache(isPrivate=false,maxAge=31536000)
	public Response getFileContents(@PathParam("id") Integer id, @Context HttpServletResponse response) throws SQLException, IOException {
		com.wordrails.business.File file = fileRepository.findOne(id);
		if (file == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			FileContents contents = contentsRepository.findOne(id);
			if (contents.contents == null) {
				return Response.status(Status.NO_CONTENT).build();	
			} else {
				response.setHeader("Pragma","public");
				response.setHeader("Cache-Control", "max-age=2592000");
				
				Calendar c = Calendar.getInstance();
	            c.setTime( new Date() );
	            c.add( Calendar.DATE, 30);
	            //HTTP header date format: Thu, 01 Dec 1994 16:00:00 GMT
	            String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format( c.getTime() );            
	            response.setHeader( "Expires", o );
	            
	            final InputStream in = contents.contents.getBinaryStream();
	            
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            int data = in.read();
	            while (data >= 0) {
	              out.write((char) data);
	              data = in.read();
	            }
	            out.flush();
	                 
	            ResponseBuilder builder = Response.ok(out.toByteArray());
	            builder.header("Content-Disposition", "attachment; filename=" + file.name);
	            
				return builder.build();
			}
		}
	}
}