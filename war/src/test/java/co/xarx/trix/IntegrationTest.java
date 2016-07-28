package co.xarx.trix;

import co.xarx.trix.config.AclTestConfig;
import co.xarx.trix.config.ApplicationTestConfig;
import co.xarx.trix.config.DatabaseTestConfig;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.domain.UserGrantedAuthority;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.persistence.UserGrantedAuthorityRepository;
import co.xarx.trix.persistence.UserRepository;
import co.xarx.trix.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.PostConstruct;

@ContextConfiguration(classes = {ApplicationTestConfig.class, DatabaseTestConfig.class, AclTestConfig.class})
public abstract class IntegrationTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserGrantedAuthorityRepository grantedAuthorityRepository;


	@PostConstruct
	public void setUp() throws Exception {
		TenantContextHolder.setCurrentTenantId("test");
		anonymousLogin();

		User adminUser = new User();
		adminUser.setUsername("admin");
		adminUser.setPassword("admin");
		adminUser = userRepository.save(adminUser);

		UserGrantedAuthority adminGA = new UserGrantedAuthority(adminUser, "ROLE_ADMIN");
		grantedAuthorityRepository.save(adminGA);

		adminUser = userRepository.findOne(adminUser.getId());

		Person admin = new Person();
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.setEmail("admin@admin.com");
		admin.setUser(adminUser);
		personRepository.save(admin);
	}

	protected void anonymousLogin() {
		AnonymousAuthenticationToken anonymous = new AnonymousAuthenticationToken("anonymousKey",
				Constants.Authentication.ANONYMOUS_USER, Constants.Authentication.ANONYMOUS_USER.authorities);
		SecurityContextHolder.getContext().setAuthentication(anonymous);
	}
}