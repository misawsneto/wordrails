package co.xarx.trix.web.rest;


import co.xarx.trix.services.EmailService;
import co.xarx.trix.services.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/util")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UtilResource {

	@Context
	private HttpServletRequest request;

	private boolean isLocal(String host) {
		return host.contains("0:0:0:0:0:0:0") ||
				host.contains("0.0.0.0") ||
				host.contains("localhost") ||
				host.contains("127.0.0.1") ||
				host.contains("xarxlocal.com");
	}

	@Autowired
	private MobileService gcmService;
	@Autowired
	private EmailService emailService;

	@GET
	@Path("/testNotification")
	public Response updateDefaultStationPerspective(@Context HttpServletRequest request) {
		if (isLocal(request.getHeader("Host"))) {
//			gcmService.init();
//
//			String adriel = "APA91bFjysKjRaAIfZM8NGX84bCaLPlGWGhtcoe6shOg2sfYXF4uJtSZaHN_lGzFUZXG1_ojdR6rFRdmUDAYxM1VHd0tLhlRXyiMjTzF8uZFmJ5wZaGR27vCjEuStqZT82F8ouUXZfW1";
//			List<String> adriels = new ArrayList<>();
//			adriels.add(adriel);
//			String hash = StringUtil.generateRandomString(10, "Aa#");
//			try {
//				Message message = new Message.Builder().addData("message", "DNSIJBVS").build();
//				gcmService.sendBulkMessages(message, adriels, hash);
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//
//			return Response.status(Response.Status.OK).build();
//		} else {
//			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.OK).build();
	}

	public static class InvitationListDto{
		public Map<String,String> emails;
		public List<Integer> stationIds;
		public String tenantId;
		public String emailSubject;
	}

	@POST
	@Path("/inviteUsers")
	@Transactional
	public Response inviteNewUsers(InvitationListDto invitationListDto){
		emailService.batchInvitation(invitationListDto.emails, invitationListDto.tenantId, invitationListDto.emailSubject, invitationListDto.stationIds);
		return Response.ok().build();
	}
}
