package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.PasswordReset;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PasswordResetRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.UserRepository;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordService {

	private EmailService emailService;
	private UserRepository userRepository;
	private PersonRepository personRepository;
	private NetworkRepository networkRepository;
	private PasswordResetRepository passwordResetRepository;

	@Autowired
	public PasswordService(PersonRepository personRepository, UserRepository userRepository, EmailService emailService,
						   PasswordResetRepository passwordResetRepository, NetworkRepository networkRepository) {
		this.emailService = emailService;
		this.userRepository = userRepository;
		this.personRepository = personRepository;
		this.networkRepository = networkRepository;
		this.passwordResetRepository = passwordResetRepository;
	}

	public User resetPassword(String email){
		Assert.hasText(email, "Null email");
		Person person = personRepository.findByEmail(email);

		if(person != null && person.user == null){
			return null;
		}

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setUser(person.user);
		passwordReset.setHash(UUID.randomUUID().toString());

		passwordResetRepository.save(passwordReset);
		emailService.sendSimpleMail(email, "Recuperação de senha", createEmailBody(person, passwordReset.hash));

		return person.user;
	}

	@Transactional
	public void updatePassword(String hash, String password) throws BadRequestException {
		if(password == null || password.isEmpty()) {
			throw new BadRequestException("Null password");
		}

		PasswordReset passwordReset = passwordResetRepository.findByHash(hash);

		if (passwordReset == null) {
			throw new BadRequestException("No hash to recover password");
		}

		passwordReset.user.password = password;
		userRepository.save(passwordReset.user);

		passwordResetRepository.deleteByUserId(passwordReset.user.id);
	}

	public String createEmailBody(Person username, String hash) {
		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());
		network.tenantId = TenantContextHolder.getCurrentTenantId();
		String baseUrl = "http://" + network.getRealDomain() + "/access/newpwd?hash=" + hash;

		return "Oi, " + username.getUser().getUsername() + ". Clique aqui para recuperar sua senha: " + baseUrl;
	}
}
