package co.xarx.trix.services;

import co.xarx.trix.api.StationRolesUpdate;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.person.PersonAlreadyExistsException;
import co.xarx.trix.services.person.PersonFactory;
import co.xarx.trix.services.person.PersonNotFoundException;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.services.user.UserAlreadyExistsException;
import co.xarx.trix.services.user.UserFactory;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.api.v1.PersonsApi;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.common.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Slf4j
@Service
public class PersonService {

	private UserFactory userFactory;
	private AuthService authService;
    private EmailService emailService;
    private PersonFactory personFactory;
    private NetworkService networkService;
    private PostRepository postRepository;
    private ImageRepository imageRepository;
    private PersonRepository personRepository;
    private CommentRepository commentRepository;
    private StationRepository stationRepository;
    private ESPersonRepository esPersonRepository;
    private InvitationRepository invitationRepository;
    private MobileDeviceRepository mobileDeviceRepository;
    private StationPermissionService stationPermissionService;
	private PersonValidationRepository personValidationRepository;
	private PersonalNotificationRepository personalNotificationRepository;

	@Autowired
	public PersonService(PersonRepository personRepository, EmailService emailService, InvitationRepository invitationRepository, AuthService authService, StationRepository stationRepository, PersonFactory personFactory, PostRepository postRepository, ImageRepository imageRepository, MobileDeviceRepository mobileDeviceRepository, StationPermissionService stationPermissionService, PersonValidationRepository personValidationRepository, UserFactory userFactory, NetworkService networkService, CommentRepository commentRepository, ESPersonRepository esPersonRepository, PersonalNotificationRepository personalNotificationRepository) {
		this.authService = authService;
        this.userFactory = userFactory;
        this.emailService = emailService;
        this.personFactory = personFactory;
        this.networkService = networkService;
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.personRepository = personRepository;
        this.commentRepository = commentRepository;
        this.stationRepository = stationRepository;
        this.esPersonRepository = esPersonRepository;
        this.invitationRepository = invitationRepository;
        this.mobileDeviceRepository = mobileDeviceRepository;
        this.stationPermissionService = stationPermissionService;
        this.personValidationRepository = personValidationRepository;
        this.personalNotificationRepository = personalNotificationRepository;
	}

    @Transactional
	public Person createFromInvite(PersonsApi.PersonCreateDto dto, String inviteHash) throws PersonAlreadyExistsException, UserAlreadyExistsException {
		Assert.hasText(inviteHash);

		User newUser = userFactory.create(dto.username, dto.password);
		Person newPerson = personFactory.create(dto.name, dto.email, newUser);

		Invitation invitation = invitationRepository.findByHash(inviteHash);

		if (invitation != null
				&& invitation.email == newPerson.email
				&& invitation.invitationStations != null
				&& invitation.invitationStations.size() > 0) {
			StationRolesUpdate update = new StationRolesUpdate();
			update.stationsIds = invitation.invitationStations;
			update.usernames = Arrays.asList(newPerson.username);
			stationPermissionService.updateStationsPermissions(update, authService.getLoggedUsername());
		}

		return newPerson;
	}

	@Transactional
	public Person createPerson(PersonsApi.PersonCreateDto dto) throws PersonAlreadyExistsException, UserAlreadyExistsException, IOException {
		if (dto.password == null || dto.password.isEmpty())
			dto.password = StringUtil.generateRandomString(6, "aA#");

		User newUser = userFactory.create(dto.username, dto.password);
		Person newPerson = personFactory.create(dto.name, dto.email, newUser);

		Network network = networkService.getNetwork();

		PersonValidation validation = new PersonValidation();
		validation.setPerson(newPerson);
		personValidationRepository.save(validation);

		try {
			emailService.validatePersonCreation(network, validation, networkService.getValidationMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPerson;
	}

	@Transactional
	public Person addPerson(PersonsApi.PersonCreateDto dto) throws PersonAlreadyExistsException, UserAlreadyExistsException {
		if(dto.password == null || dto.password.isEmpty())
			dto.password = StringUtil.generateRandomString(6, "aA#");

		User newUser = userFactory.create(dto.username, dto.password);
		Person newPerson = personFactory.create(dto.name, dto.email, newUser);

		Network network = networkService.getNetwork();
		try {
			emailService.sendCredentials(network, authService.getLoggedPerson(), newPerson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPerson;
	}

	public Person validatePersonEmail(String hash) throws BadRequestException {
		PersonValidation validation = personValidationRepository.findByValidationHash(hash);

		if(validation == null && validation.getPerson() != null){
			return null;
		}

		Network network = networkService.getNetwork();

		Person person = validation.getPerson();
		validation.validated = true;

		if (network.isEmailSignUpValidationEnabled()) {
			person.user.enabled = true;
			personRepository.save(person);
		}

		personValidationRepository.save(validation);
		return person;
	}

	private void cleanInvitationDto(PersonsApi.PersonInvitationDto dto){
		removeRepeatedPerson(dto.emails);
		removeInvalidStations(dto.stationIds);

		dto.emailTemplate = dto.emailTemplate.replaceAll("\\&\\#123;\\&\\#123;", "{{");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\&\\#125;\\&\\#125;", "}}");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\%7B\\%7B", "{{");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\%7D\\%7D", "}}");
	}

	private void removeInvalidStations(List stations){
		if (stations != null) {
			for(Iterator<Integer> it = stations.iterator(); it.hasNext();){
				Integer id = it.next();
				if (stationRepository.findOne(id) == null) {
					it.remove();
				}
			}
		}
	}

	private void removeRepeatedPerson(List emails){
		List<Person> persons = personRepository.findByEmailIn(emails);

		if (persons == null || persons.size() <= 0) return;

		for(Person person: persons){
            if(emails.contains(person.getEmail())){
                emails.remove(person.getEmail());
                Logger.debug(person.getEmail() + " already is an registered user -- removing from invitees");
            }
        }
	}

	public List<String> invite(PersonsApi.PersonInvitationDto dto){
		if(dto.emails == null || dto.emails.size() == 0 || dto.emailTemplate == null){
			throw new BadRequestException("Invalid emails or template");
		}

		cleanInvitationDto(dto);
		Network network = networkService.getNetwork();

		for(String email: dto.emails){
			Invitation invitation = new Invitation(network.getRealDomain());
			invitation.email = email;
			invitation.invitationStations = dto.stationIds;
			invitationRepository.save(invitation);
			if(dto.emailTemplate.equals("") || dto.emailTemplate == null){
				dto.emailTemplate = network.getInvitationMessage();
			}
			emailService.sendInvitation(network, invitation, authService.getLoggedPerson(), dto.emailTemplate);
		}

		return dto.emails;
	}

	@Transactional
	public void deletePerson(String email) throws PersonNotFoundException {
		Person person = personRepository.findByEmail(email);

		if(person == null) {
            invitationRepository.deleteByEmail(email);
        } else {
            deletePersonAndDependencies(person);
			userFactory.delete(person.getUsername());
		}
	}

	private void deletePersonAndDependencies(Person person){
        Person admin = authService.getLoggedPerson();

        invitationRepository.deleteByEmail(person.getEmail());
        personValidationRepository.deleteByPersonId(person.getId());
        personalNotificationRepository.deleteByPersonId(person.getId());
        commentRepository.updateCommentsAuthor(person.getId(), admin.getId());
        postRepository.updatePostAuthor(person.getId(), admin.getId());

        mobileDeviceRepository.deleteByPersonId(person.getId());
        esPersonRepository.delete(person.getId());

        if(person.cover != null) imageRepository.delete(person.cover);
        if(person.image != null) imageRepository.delete(person.image);

        personRepository.delete(person);
    }
}
