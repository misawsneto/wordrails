package co.xarx.trix.services;

import co.xarx.trix.api.StationRolesUpdate;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.person.PersonAlreadyExistsException;
import co.xarx.trix.services.person.PersonFactory;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.services.user.UserAlreadyExistsException;
import co.xarx.trix.services.user.UserFactory;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.api.v1.PersonsApi;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.jcodec.common.logging.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Slf4j
@Service
public class PersonService {

	private Client client;
	private String nginxAccessIndex;
	private UserFactory userFactory;
	private AuthService authService;
	private EmailService emailService;
	private PersonFactory personFactory;
	private PersonRepository personRepository;
	private NetworkRepository networkRepository;
	private StationRepository stationRepository;
	private InvitationRepository invitationRepository;
	private StationPermissionService stationPermissionService;
	private PersonValidationRepository personValidationRepository;

	@Autowired
	public PersonService(PersonRepository personRepository, EmailService emailService, NetworkRepository networkRepository, InvitationRepository invitationRepository, AuthService authService, Client client, @Value("${elasticsearch.nginxAccessIndex}") String nginxAccessIndex, StationRepository stationRepository, PersonFactory personFactory, StationPermissionService stationPermissionService, PersonValidationRepository personValidationRepository, UserFactory userFactory) {
		this.client = client;
		this.authService = authService;
		this.userFactory = userFactory;
		this.emailService = emailService;
		this.personFactory = personFactory;
		this.nginxAccessIndex = nginxAccessIndex;
		this.personRepository = personRepository;
		this.stationRepository = stationRepository;
		this.networkRepository = networkRepository;
		this.invitationRepository = invitationRepository;
		this.stationPermissionService = stationPermissionService;
		this.personValidationRepository = personValidationRepository;
	}

	public Person createFromInvite(PersonsApi.PersonCreateDto dto, String inviteHash) throws PersonAlreadyExistsException, UserAlreadyExistsException {
		Assert.hasText(inviteHash);

		User newUser = userFactory.create(dto.username, dto.password);
		Person newPerson = null;
		try {
			newPerson = personFactory.create(dto.name, dto.email, newUser);
		} catch (Exception e) {
			userFactory.delete(dto.username);
			throw e;
		}

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
	public Person createPerson(PersonsApi.PersonCreateDto dto) throws PersonAlreadyExistsException, UserAlreadyExistsException {
		if (dto.password == null || dto.password.isEmpty())
			dto.password = StringUtil.generateRandomString(6, "aA#");

		User newUser = userFactory.create(dto.username, dto.password);
		Person newPerson = null;
		try{
			newPerson = personFactory.create(dto.name, dto.email, newUser);
		}catch (Exception e){
			userFactory.delete(dto.username);
			throw e;
		}

		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());

		PersonValidation validation = new PersonValidation();
		validation.setPerson(newPerson);
		personValidationRepository.save(validation);

		try {
			emailService.validatePersonCreation(network, authService.getLoggedPerson(), validation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPerson;
	}

	public Person addPerson(PersonsApi.PersonCreateDto dto) throws PersonAlreadyExistsException, UserAlreadyExistsException {
		if(dto.password == null || dto.password.isEmpty())
			dto.password = StringUtil.generateRandomString(6, "aA#");

		User newUser = userFactory.create(dto.username, dto.password);
		Person newPerson = null;
		try {
			newPerson = personFactory.create(dto.name, dto.email, newUser);
		} catch (Exception e) {
			userFactory.delete(dto.username);
			throw e;
		}

		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());

		try {
			emailService.sendCredentials(network, authService.getLoggedPerson(), newPerson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPerson;
	}

	public Person validateEmail(String hash) throws BadRequestException {
		PersonValidation validation = personValidationRepository.findByValidationHash(hash);

		if(validation == null && validation.getPerson() != null){
			return null;
		}

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		Person person = validation.getPerson();
		validation.validated = true;

		if (network.isEmailSignUpValidationEnabled()) {
			person.user.enabled = true;
			personRepository.save(person);
		}

		personValidationRepository.save(validation);
		return person;
	}

	public List<Person> invite(PersonsApi.PersonInvitationDto dto){
		if(dto.emails == null || dto.emails.size() == 0 || dto.emailTemplate == null){
			throw new BadRequestException("Invalid emails or template");
		}

		List<Person> persons = personRepository.findByEmailIn(dto.emails);

		if(persons != null && persons.size() > 0){
			for (Iterator<String> iterator = dto.emails.iterator(); iterator.hasNext();) {
				String email = iterator.next();
				
				persons.stream().filter(p -> p.email.equals(email)).forEach(p -> {
					iterator.remove();
					Logger.debug(email + " already is an registered user -- removing from invitees");
				});
			}
		}

		if (dto.stationIds != null) {
			for(Iterator<Integer> it = dto.stationIds.iterator(); it.hasNext();){
				Integer id = it.next();
				if (stationRepository.findOne(id) == null) {
					it.remove();
				}
			}
		}

		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());

		dto.emailTemplate = dto.emailTemplate.replaceAll("\\&\\#123;\\&\\#123;", "{{");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\&\\#125;\\&\\#125;", "}}");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\%7B\\%7B", "{{");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\%7D\\%7D", "}}");

		for(String email: dto.emails){
			Invitation invitation = new Invitation(network.getRealDomain());
			invitation.email = email;
			invitation.invitationStations = dto.stationIds;
			invitationRepository.save(invitation);
			if(dto.emailTemplate.equals("")){
				dto.emailTemplate = network.getInvitationMessage();
			}
			emailService.sendInvitation(network, invitation, authService.getLoggedPerson(), dto.emailTemplate);
		}

		return persons;
	}

	public List<Map<String, Object>> getUserTimeline(String username) {
		Person person = personRepository.findByUsername(username);
		Assert.notNull(person, "Person not found: personId " + username);

		List<Map<String, Object>> timeline = new ArrayList<>();

		SearchRequestBuilder search = client.prepareSearch(nginxAccessIndex);
		search.setQuery(boolQuery().must(termQuery("username", person.username)));

		SearchResponse searchResponse = search.execute().actionGet();

		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

		SearchHit[] hits = searchResponse.getHits().getHits();
		for (SearchHit hit : hits) {
			Map source = hit.sourceAsMap();

			int response = (int) source.get("response");
			if (response < 200 || response >= 400) {
				continue;
			}

			Long timestamp = fmt.parseDateTime(source.get("@timestamp").toString()).getMillis()/1000;
			source.put("date", timestamp);
			source.remove("message");
			source.remove("host");
			source.remove("type");
			source.remove("clientip");
			source.remove("ZONE");
			source.remove("verb");
			source.remove("httpversion");
			source.remove("bytes");
			source.remove("referrer");
			source.remove("os_name");
			source.remove("device");
			source.remove("browser");

			timeline.add(source);
		}

		return timeline;
	}
}
