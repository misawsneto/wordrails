package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.PasswordReset;
import co.xarx.trix.domain.User;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PasswordResetRepository;
import co.xarx.trix.persistence.UserRepository;
import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordService {

	private PersonRepository personRepository;
	private UserRepository userRepository;
	private EmailService emailService;
	private PasswordResetRepository passwordResetRepository;
	private NetworkRepository networkRepository;

	@Autowired
	public PasswordService(PersonRepository personRepository, UserRepository userRepository,
						   EmailService emailService, NetworkRepository networkRepository, PasswordResetRepository passwordResetRepository){
		this.personRepository = personRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.passwordResetRepository = passwordResetRepository;
		this.networkRepository = networkRepository;
	}

	public void resetPassword(String email){
		Assert.hasText(email, "Null email");
		User user = userRepository.findUserByEmail(email);

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setUser(person.user);
		passwordReset.setHash(UUID.randomUUID().toString());

		passwordResetRepository.save(passwordReset);

		emailService.sendSimpleMail(email, "Recuperação de senha", createEmailBody(user.getUsername(), passwordReset.hash));
	}

	@Transactional
	public void updatePassword(String hash, String password){
		Assert.hasText(password, "Null password");

		PasswordReset passwordReset = passwordResetRepository.findByHash(hash);

		Assert.notNull(passwordReset, "No hash to recover password");

		passwordReset.user.password = password;
		userRepository.save(passwordReset.user);

		passwordResetRepository.deleteByUserId(passwordReset.user.id);
	}

	public String createEmailBody(String username, String hash) {
		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());
		String baseUrl = "http://" + network.getRealDomain() + "/access/newpwd?hash=" + hash;

		return "Oi, " + username + ". Clique aqui para recuperar sua senha: " + baseUrl;
	}
}
