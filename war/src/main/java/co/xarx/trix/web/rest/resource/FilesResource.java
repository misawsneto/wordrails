package co.xarx.trix.web.rest.resource;

import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.FilesApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@NoArgsConstructor
public class FilesResource extends AbstractResource implements FilesApi {

	private FileRepository fileRepository;
	private AmazonCloudService amazonCloudService;

	@Autowired
	public FilesResource(FileRepository fileRepository, AmazonCloudService amazonCloudService) {
		this.fileRepository = fileRepository;
		this.amazonCloudService = amazonCloudService;
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


}