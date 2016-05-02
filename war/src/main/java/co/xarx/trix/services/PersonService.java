package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.InvitationRepository;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.api.v1.PersonsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
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

	/**
	 *
	 * @param dto
	 * @return conflicting person
	 */
	public List<Person> invite(PersonsApi.PersonInvitationDto dto){
		if(dto.emails == null || dto.emails.size() == 0 || dto.emailTemplate == null){
			throw new BadRequestException("Invalid emails");
		}

		List<Person> persons = personRepository.findByEmailIn(dto.emails);

		if(persons != null && persons.size() > 0){
			for (Iterator<String> iterator = dto.emails.iterator(); iterator.hasNext();) {
				String email = iterator.next();
				for (Person p: persons) {
					if (p.email.equals(email))
						iterator.remove();
				}
			}
		}

		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());

		for(String email: dto.emails){
			Invitation invitation = new Invitation(network.getRealDomain(), false);
			invitationRepository.save(invitation);
			emailService.sendInvitation(network, invitation, authService.getLoggedPerson());
		}

		return persons;
	}

	public Person create(String name, String username, String password, String email, List<StationRole> stationsRole) {
		if (personRepository.findOne(QPerson.person.username.eq(username)) != null) {
			throw new AlreadyExistsException("User with username " + username + " already exists");
		} else if (personRepository.findOne(QPerson.person.email.eq(email)) != null) {
			throw new AlreadyExistsException("User with email " + email + " already exists");
		}

		boolean sendPlainPassword = false;
		if(password == null || "".equals(password)){
			password = StringUtil.generateRandomString(6, "aA#");
		} else {
			sendPlainPassword = true;
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

		notifyPersonCreation(person, sendPlainPassword);
		return person;
	}

	public void notifyPersonCreation(Person person, boolean sendPlainPassword){
		Network network = networkRepository.findByTenantId(person.getTenantId());
		Invitation invitation = new Invitation(network.getRealDomain(), sendPlainPassword);
		invitation.setPerson(person);

		invitationRepository.save(invitation);

		emailService.notifyPersonCreation(network, invitation, authService.getLoggedPerson());
	}
}
