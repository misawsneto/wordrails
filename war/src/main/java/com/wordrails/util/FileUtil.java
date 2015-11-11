package com.wordrails.util;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FileUtil {

	public static FileItem getFileFromRequest(HttpServletRequest request) throws FileUploadException {
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
}
