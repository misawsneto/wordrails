package com.wordrails.api;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.wordrails.business.PasswordReset;
import com.wordrails.business.Person;
import com.wordrails.business.User;
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
	private @Autowired UserDetailsManager userDetailsManager;
	private @Autowired UserRepository userRepository; 

	@POST
	@Path("/")
	@Transactional
	public Response postPasswordReset(PasswordReset passwordReset) throws ServletException, IOException{

		if(passwordReset.email == null || personRepository.findByEmail(passwordReset.email) == null)
			return Response.status(Status.NOT_FOUND).build();

		if(passwordReset.invite && passwordReset.networkSubdomain == null){
			return Response.status(Status.BAD_REQUEST).build();
		}
			
		// TODO: check how many request for password has been sent for a given email in the last minute, passwordReset.createdAt and add 
		passwordReset.hash = UUID.randomUUID().toString();
		passwordResetRepository.save(passwordReset);
		
		if(passwordReset.invite) 
			sendInviteEmail(passwordReset);
		else
			sendResetEmail(passwordReset);
		
		return Response.status(Status.CREATED).build();
//		throw new UnauthorizedException();
	}

	private void sendResetEmail(PasswordReset passwordReset) {
		// TODO create and send Reset email only
	}

	private void sendInviteEmail(PasswordReset passwordReset) {
		// TODO create and send invite and reset email
	}

	@PUT
	@Path("/{hash}")
	@Transactional
	public Response putPasswordReset(@PathParam("hash") String hash, @FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword) throws ServletException, IOException{
		PasswordReset pr = passwordResetRepository.findByHash(hash);
		if(pr ==null ){
			return Response.status(Status.NOT_FOUND).build();
		}else if(!pr.active){
			return Response.status(Status.GONE).build();
		}

		Person person = personRepository.findByEmail(pr.email);
		person.passwordReseted = true;
		if(!person.username.equalsIgnoreCase("wordrails")){ // don't allow users to change wordrails pas
			User user = userRepository.findByUsernameAndPassword(person.username, oldPassword);
			if(user != null){
				user.password = newPassword;
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
