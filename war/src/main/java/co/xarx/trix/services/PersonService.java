package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Invitation;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.QPerson;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.InvitationRepository;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.api.v1.PersonsApi;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Slf4j
@Service
public class PersonService {

	private Client client;
	private String nginxAccessIndex;
	private AuthService authService;
	private UserService userService;
	private EmailService emailService;
	private PersonRepository personRepository;
	private NetworkRepository networkRepository;
	private InvitationRepository invitationRepository;

	@Autowired
	public PersonService(UserService userService, PersonRepository personRepository, EmailService emailService,
						 NetworkRepository networkRepository, InvitationRepository invitationRepository,
						 AuthService authService, Client client, @Value("${elasticsearch.nginxAccessIndex}") String nginxAccessIndex) {
		this.client = client;
		this.authService = authService;
		this.userService = userService;
		this.emailService = emailService;
		this.nginxAccessIndex = nginxAccessIndex;
		this.personRepository = personRepository;
		this.networkRepository = networkRepository;
		this.invitationRepository = invitationRepository;
	}

	/**
	 *
	 * @param dto
	 * @return conflicting person
	 */
	public List<Person> invite(PersonsApi.PersonInvitationDto dto){
		if(dto.emails == null || dto.emails.size() == 0 || dto.emailTemplate == null){
			throw new BadRequestException("Invalid emails or temaplte");
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

		dto.emailTemplate = dto.emailTemplate.replaceAll("\\&\\#123;\\&\\#123;", "{{");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\&\\#125;\\&\\#125;", "}}");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\%7B\\%7B", "{{");
		dto.emailTemplate = dto.emailTemplate.replaceAll("\\%7D\\%7D", "}}");

		for(String email: dto.emails){
			Invitation invitation = new Invitation(network.getRealDomain(), false);
			invitation.email = email;
			invitationRepository.save(invitation);
			emailService.sendInvitation(network, invitation, authService.getLoggedPerson(), dto.emailTemplate);
		}

		return persons;
	}

	public Person create(String name, String username, String password, String email) {
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
