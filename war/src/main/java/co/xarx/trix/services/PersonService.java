package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.InvitationRepository;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.services.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	private UserService userService;
	private PersonRepository personRepository;
	private EmailService emailService;
	private NetworkRepository networkRepository;
	private StationRolesRepository stationRolesRepository;
	private InvitationRepository invitationRepository;
	private AuthService authService;

	@Autowired
	public PersonService(UserService userService, PersonRepository personRepository, EmailService emailService,
						 NetworkRepository networkRepository, StationRolesRepository stationRolesRepository,
						 InvitationRepository invitationRepository, AuthService authService) {
		this.userService = userService;
		this.personRepository = personRepository;
		this.emailService = emailService;
		this.networkRepository = networkRepository;
		this.stationRolesRepository = stationRolesRepository;
		this.invitationRepository = invitationRepository;
		this.authService = authService;
	}

	@Transactional
	public boolean toggleRecommend(Person person, Integer postId) {
		boolean recommendInserted = false;
		if (person.getRecommendPosts().contains(postId)) {
			person.getRecommendPosts().remove(postId);
		} else {
			recommendInserted = true;
			person.getRecommendPosts().add(postId);
		}

		return recommendInserted;
	}

	@Transactional
	public boolean toggleBookmark(Person person, Integer postId) {
		boolean bookmarkInserted = false;
		if (person.getBookmarkPosts().contains(postId)) {
			person.getBookmarkPosts().remove(postId);
		} else {
			bookmarkInserted = true;
			person.getBookmarkPosts().add(postId);
		}

		return bookmarkInserted;
	}

	public Person create(String name, String username, String password, String email, boolean emailNotification, List<StationRole> stationsRole) {
		if (personRepository.findOne(QPerson.person.username.eq(username)) != null) {
			throw new AlreadyExistsException("User with username " + username + " already exists");
		} else if (personRepository.findOne(QPerson.person.email.eq(email)) != null) {
			throw new AlreadyExistsException("User with email " + email + " already exists");
		}

		Person person = new Person();
		person.name = name;
		person.username = username;
		person.password = password;
		person.email = email;

		person.user = userService.create(username, password);

		personRepository.save(person);

		if(stationsRole != null){
			for (StationRole stationRole : stationsRole) {
				stationRolesRepository.save(stationRole);
			}
		}

		if(emailNotification) invitePerson(person);
		return person;
	}

	public void invitePerson(Person person){
		Network network = networkRepository.findByTenantId(person.getTenantId());
		Invitation invitation = new Invitation(network.getRealDomain());
		invitation.setPerson(person);

		invitationRepository.save(invitation);

		emailService.sendNetworkInvitation(network, invitation, authService.getLoggedPerson());
	}
}
