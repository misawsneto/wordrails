package co.xarx.trix.services.user;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.User;
import co.xarx.trix.domain.UserGrantedAuthority;
import co.xarx.trix.domain.social.SocialUser;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.UserGrantedAuthorityRepository;
import co.xarx.trix.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@IntegrationTestBean
@Service
public class UserFactory {

	private UserRepository userRepository;
	private NetworkRepository networkRepository;
	private UserConnectionFactory userConnectionFactory;
	private UserGrantedAuthorityRepository grantedAuthorityRepository;

	@Autowired
	public UserFactory(UserRepository userRepository, UserConnectionFactory userConnectionFactory, UserGrantedAuthorityRepository grantedAuthorityRepository, NetworkRepository networkRepository) {
		this.userRepository = userRepository;
		this.userConnectionFactory = userConnectionFactory;
		this.grantedAuthorityRepository = grantedAuthorityRepository;
		this.networkRepository = networkRepository;
	}

	public User create(String username, String password) throws UserAlreadyExistsException {
		if(userRepository.existsByUsername(username)) {
			throw new UserAlreadyExistsException();
		}

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		User user = new User();
		user.enabled = !network.isEmailSignUpValidationEnabled();
		user.username = username;
		user.password = password;

		user = userRepository.save(user);
		UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.USER);
		grantedAuthorityRepository.save(authority);

		return user;
	}

	public User create(String username, SocialUser socialUser) throws UserAlreadyExistsException {
		if (userRepository.existsByUsername(username)) {
			throw new UserAlreadyExistsException();
		}

		User user = new User();
		user.enabled = true;
		user.username = username;
		user.password = "";

		user = userRepository.save(user);

		userConnectionFactory.create(user, socialUser);

		UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.USER);
		grantedAuthorityRepository.save(authority);

		return user;
	}
}
