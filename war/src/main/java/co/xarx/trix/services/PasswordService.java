package co.xarx.trix.services;

import co.xarx.trix.domain.PasswordReset;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PasswordResetRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.UserRepository;
import com.mysema.commons.lang.Assert;
import org.hibernate.metamodel.relational.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PasswordService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordResetRepository passwordResetRepository;

	public void resetPassword(String email){
		Assert.hasText(email, "Null email");

		Person person = personRepository.findByEmail(email);

		Assert.notNull(person, "Person not found");

//		PasswordReset checkReset = passwordResetRepository.findOne(QPasswordReset.passwordReset.user.eq(person.user));

		PasswordReset checkReset = passwordResetRepository.findOne(person.user.id);

		if( checkReset != null && checkReset.expiresAt.after(new Date()) ){
			if(checkReset.expiresAt.before(new Date())){
				return;
			}
			throw new IllegalArgumentException();
		}

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.user = person.user;
		passwordReset.hash = UUID.randomUUID().toString();

		passwordResetRepository.save(passwordReset);

		String emailBody = "Hi, " + person.getName() + "\n Here is your hash: " + passwordReset.hash + "\n";
		String emailSubject = "We'll help you";

		emailService.sendSimpleMail(person.getEmail(), emailSubject, emailBody);
	}

	public void updatePassword(String hash, String password){
		Assert.hasText(password, "Null password");

		PasswordReset passwordReset = passwordResetRepository.findByHash(hash);

		if(passwordReset == null){
			throw new IllegalIdentifierException("No hash to recover password");
		}

		passwordReset.user.password = password;
		userRepository.save(passwordReset.user);

		passwordResetRepository.delete(passwordReset);
	}


//	private void sendInviteEmail(PasswordReset passwordReset) {
//		try {
//			String filePath = getClass().getClassLoader().getResource("tpl/invitation-email.html").getFile();
//
//			filePath = System.getProperty("os.name").contains("indow") ? filePath.substring(1) : filePath;
//
//			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
//			String template = new String(bytes, Charset.forName("UTF-8"));
//
//			HashMap<String, Object> scopes = new HashMap<>();
//			scopes.put("name", passwordReset.personName + "");
//			scopes.put("networkName", passwordReset.networkName + "");
//			scopes.put("link", "http://" + passwordReset.getTenantId() + ".xarx.co/#/pass?hash=" + passwordReset.hash);
//			scopes.put("networkSubdomain", passwordReset.getTenantId());
//			scopes.put("passwordReset", passwordReset);
//
//			Person person = authProvider.getLoggedPerson();
//			if (person != null) scopes.put("inviterName", person.name);
//			else scopes.put("inviterName", "");
//
//			StringWriter writer = new StringWriter();
//
//			MustacheFactory mf = new DefaultMustacheFactory();
//
//			Mustache mustache = mf.compile(new StringReader(template), "invitation-email");
//			mustache.execute(writer, scopes);
//			writer.flush();
//
//			String emailBody = writer.toString();
//			String subject = "[" + passwordReset.networkName + "]" + " Cadastro de senha";
//
//			//emailService.sendSimpleMail(passwordReset.email, subject, emailBody);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
