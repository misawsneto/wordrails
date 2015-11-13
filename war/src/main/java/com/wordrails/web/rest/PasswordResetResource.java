package com.wordrails.web.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wordrails.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.wordrails.WordrailsService;
import com.wordrails.domain.EmailService;
import com.wordrails.domain.PasswordReset;
import com.wordrails.domain.Person;
import com.wordrails.domain.User;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PasswordResetRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.security.NetworkSecurityChecker;

@Path("/passwordResets")
@Consumes(MediaType.WILDCARD)
@Component
public class PasswordResetResource {
	@Autowired private NetworkSecurityChecker networkSecurityChecker;
	@Autowired private PasswordResetRepository passwordResetRepository;
	@Autowired private PersonRepository personRepository;
	@Autowired private NetworkRepository networkRepository; 
	private @Autowired UserRepository userRepository;
	private @Autowired EmailService emailService;
	private @Autowired
	TrixAuthenticationProvider authProvider;
	private @Autowired WordrailsService wordrailsService;

	@POST
	@Path("/")
	@Transactional
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postPasswordReset(PasswordReset passwordReset) throws ServletException, IOException{

		if(passwordReset.email == null || personRepository.findByEmail(passwordReset.email) == null)
			return Response.status(Status.NOT_FOUND).build();

		if(passwordReset.invite && passwordReset.networkSubdomain == null){
			return Response.status(Status.BAD_REQUEST).build();
		}

		// TODO: check how many request for password has been sent for a given email in the last minute, passwordReset.createdAt and add 
		passwordReset.hash = UUID.randomUUID().toString();
		passwordResetRepository.save(passwordReset);

		if(passwordReset.invite) // if passwordReset is of type user invitation, send invitation email
			sendInviteEmail(passwordReset);
		else // if not invitation, send a simple user reset email.
			wordrailsService.sendResetEmail(passwordReset);

		return Response.status(Status.CREATED).build();
		//		throw new UnauthorizedException();
	}

	private void sendInviteEmail(PasswordReset passwordReset) {
		try {
			String filePath = getClass().getClassLoader().getResource("tpl/invitation-email.html").getFile();
			
			filePath = System.getProperty( "os.name" ).contains( "indow" ) ? filePath.substring(1) : filePath;
			
			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			String template = new String (bytes, Charset.forName("UTF-8"));

			HashMap<String, Object> scopes = new HashMap<String, Object>();
			scopes.put("name", passwordReset.personName + "");
			scopes.put("networkName", passwordReset.networkName + "");
			scopes.put("link", "http://" + passwordReset.networkSubdomain + ".xarx.co/#/pass?hash=" + passwordReset.hash);
			scopes.put("networkSubdomain", passwordReset.networkSubdomain);
			scopes.put("passwordReset", passwordReset);
			
			Person person = authProvider.getLoggedPerson();
			if(person != null)
				scopes.put("inviterName", person.name);
			else
				scopes.put("inviterName", "");
			
			StringWriter writer = new StringWriter();
			
			MustacheFactory mf = new DefaultMustacheFactory();

			Mustache mustache = mf.compile(new StringReader(template), "invitation-email");
			mustache.execute(writer, scopes);
			writer.flush();
			
			String emailBody = writer.toString();
			String subject =  "[" + passwordReset.networkName + "]"  + " Cadastro de senha";
			
			emailService.sendSimpleMail(passwordReset.email, subject , emailBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * For a given hash sent by email, find the hash, check if it is active, change the password and set the {@link PasswordReset.active} to false in Database.
	 * @param hash
	 * @param password
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@PUT
	@Path("/{hash}")
	@Transactional
	public Response updatePassword(@PathParam("hash") String hash, @FormParam("password") String password) throws ServletException, IOException{
		PasswordReset pr = passwordResetRepository.findByHash(hash);
		if(pr == null ){ // if no password reset was found for this hash, return 
			return Response.status(Status.NOT_FOUND).build();
		}else if(!pr.active){ // if hash is not active, return the 410 http code
			return Response.status(Status.GONE).build();
		}

		Person person = personRepository.findByEmail(pr.email);
		person.passwordReseted = true;
		if(!person.username.equals("wordrails")){ // don't allow users to change wordrails pas
			Integer networkId = 0;
			if(person.user != null && person.user.network != null)
				networkId = person.user.network.id;
			User user = userRepository.findByUsernameAndEnabledAndNetworkId(person.username, true, networkId);
			if(user != null){
				user.password = password;
				userRepository.save(user);

				personRepository.save(person);

				pr.active = false;
				passwordResetRepository.save(pr);
				return Response.status(Status.OK).build();
			}else
				return Response.status(Status.NOT_FOUND).build();	
		}

		return Response.status(Status.BAD_REQUEST).build();
	}
}
