package co.xarx.trix.services;

import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.PasswordReset;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.NetworkRepository;
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
	@Autowired
	private NetworkRepository networkRepository;

	public void resetPassword(String email){
		Assert.hasText(email, "Null email");

		Person person = personRepository.findByEmail(email);

		Assert.notNull(person, "Person not found");
		PasswordReset checkReset = passwordResetRepository.findOne(person.user.id);

		if (checkReset == null || !checkReset.expiresAt.after(new Date())) {

			PasswordReset passwordReset = new PasswordReset();
			passwordReset.user = person.user;
			passwordReset.hash = UUID.randomUUID().toString();

			passwordResetRepository.save(passwordReset);

			emailService.sendSimpleMail(person.getEmail(), "Recuperação de senha", createEmailBody(person, passwordReset.hash));
		} else {
			if (checkReset.expiresAt.before(new Date())) {
				return;
			}
			throw new IllegalArgumentException();
		}
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

	public String createEmailBody(Person person, String hash){
		String baseUrl;

		Network network = networkRepository.findByTenantId(person.tenantId);
		baseUrl = "http://" + network.getRealDomain() + "/access/newpwd?hash=" + hash;

		return "Oi, " + person.getName() + ". Clique aqui para recuperar sua senha: " + baseUrl;
	}
}
