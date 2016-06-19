package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.ErrorData;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2MobileApi;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialBlob;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class V2MobileResource extends AbstractResource implements V2MobileApi {

	private MobileService mobileService;

	@Autowired
	public V2MobileResource(MobileService mobileService) {
		this.mobileService = mobileService;
	}

	@Override
	public Response updateAppleCertificate() throws ServletException, IOException {
		FileItem item;
		try {
			item = getFileFromRequest();
		} catch (FileUploadException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("File could not be extracted from request").build();
		}

		Blob blob;
		try {
			blob = new SerialBlob(item.get());
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File could not be converted to blob").build();
		}

		mobileService.updateAppleCertificateFile(blob);

		return Response.ok().build();
	}

	@Override
	public Response updateApplePassword(@FormParam("password") String password) throws ServletException, IOException {
		try {
			mobileService.updateApplePassword(password);
		} catch (Exception e) {
			ErrorData error = new ErrorData(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}

		return Response.ok().build();
	}
}
