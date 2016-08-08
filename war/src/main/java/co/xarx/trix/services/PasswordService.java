package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.PasswordReset;
import co.xarx.trix.domain.User;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.PasswordResetRepository;
import co.xarx.trix.persistence.UserRepository;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordService {

	private UserRepository userRepository;
	private EmailService emailService;
	private PasswordResetRepository passwordResetRepository;
	private NetworkService networkService;

	@Autowired
	public PasswordService(UserRepository userRepository, EmailService emailService,
						   PasswordResetRepository passwordResetRepository, NetworkService networkService) {
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.passwordResetRepository = passwordResetRepository;
		this.networkService = networkService;
	}

	public User resetPassword(String email){
		Assert.hasText(email, "Null email");
		User user = userRepository.findUserByEmail(email);

		if(user == null){
			return null;
		}

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setUser(user);
		passwordReset.setHash(UUID.randomUUID().toString());

		passwordResetRepository.save(passwordReset);
		emailService.sendSimpleMail(email, "Recuperação de senha", createEmailBody(user.getUsername(), passwordReset.hash));

		return user;
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

	public String createEmailBody(String username, String hash) {
		Network network = networkService.getNetwork();
		String baseUrl = "http://" + network.getRealDomain() + "/access/newpwd?hash=" + hash;

		return "Oi, " + username + ". Clique aqui para recuperar sua senha: " + baseUrl;
	}
}
