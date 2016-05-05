package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.util.Logger;
import co.xarx.trix.util.StringUtil;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Service
public class EmailService {

	private final String NO_REPLY = "noreply@trix.rocks";
	private final String USERNAME = "AKIAJKZJWC7NU3RF3URQ";
	private final String PASSWORD = "2knHnAfeG5uVXmCXcLjquUUiNnxyWjJcIuCp2mxg";
	private final String HOST = "email-smtp.us-east-1.amazonaws.com";

	private final String defaultMessage = "{{inviterName}} convidou você para fazer parte da rede {{networkName}}.";

	@Autowired
	public PersonRepository personRepository;
	@Autowired
	public UserRepository userRepository;
	@Autowired
	public NetworkRepository networkRepository;
	@Autowired
	public StationRepository stationRepository;
	@Autowired
	public StationRolesRepository stationRolesRepository;

	@Async
	public void sendSimpleMail(String emailTo, String subject, String emailBody) {
		sendSimpleMail(NO_REPLY, emailTo, subject, emailBody);
	}

	@Async
	public void sendSimpleMail(String emailFrom, String emailTo, String subject, String emailBody) {

//		 Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[]{emailTo});

		// Create the subject and body of the message.
		Content subjectc = new Content().withData(subject);
		Content textBody = new Content().withData(emailBody);
		Body body = new Body().withHtml(textBody);

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject(subjectc).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource(emailFrom).withDestination(destination).withMessage(message);

		try
		{
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(new BasicAWSCredentials(USERNAME, PASSWORD));
			Region REGION = Region.getRegion(Regions.US_EAST_1);
			client.setRegion(REGION);
			// Send the email.
			client.sendEmail(request);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Async
	public void sendNetworkInvitation(Network network, Set<Invitation> invitations){
		if(invitations!= null)
		for (Invitation i : invitations){
			sendNetworkInvitation(network, i);
		}
	}

	@Async
	public void sendNetworkInvitation(Network network, Invitation invitation){
		try {
			String filePath = getClass().getClassLoader().getResource("tpl/network-invitation-email.html").getFile();

			filePath = System.getProperty("os.name").contains("indow") ? filePath.substring(1) : filePath;

			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			String template = new String(bytes, Charset.forName("UTF-8"));
			sendNetworkInvitation(network, invitation, template);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Async
	public void sendNetworkInvitation(Network network, Invitation invitation, String template){
		try{
			Color c1 = Color.decode(network.mainColor);
			Color c2 = Color.decode(network.navbarColor);

			HashMap<String, Object> scopes = new HashMap<String, Object>();
			scopes.put("name", invitation.personName);
			scopes.put("networkName", network.name);
			scopes.put("primaryColor", "rgb(" + c1.getRed() + ", " + c1.getGreen() + ", " + c1.getBlue() + " )");
			scopes.put("secondaryColor", "rgb(" + c2.getRed() + ", " + c2.getGreen() + ", " + c2.getBlue() + " )");
			scopes.put("link", "http://" + (network.domain != null ? network.domain : network.getTenantId() + ".trix.rocks"));
			scopes.put("invitationUrl", invitation.getUrl());
			scopes.put("networkSubdomain", network.getTenantId());
			scopes.put("network", network);
			scopes.put("hash", invitation.hash);

			StringWriter writer = new StringWriter();

			MustacheFactory mf = new DefaultMustacheFactory();

			Mustache mustache = mf.compile(new StringReader(template), "invitation-email");
			mustache.execute(writer, scopes);
			writer.flush();

			String emailBody = writer.toString();
			String subject = "[ "+ network.name +" ]" + " Convite ";
			sendSimpleMail(invitation.email, subject, emailBody);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Async
	public void notifyPersonCreation(Network network, Person invitee, Person inviter) {
		try {

			String filePath = new ClassPathResource("subscription-with-password-email.html").getFile().getAbsolutePath();

			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			String template = new String(bytes, Charset.forName("UTF-8"));
			notifyPersonCreation(network, invitee, template, inviter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	public void notifyPersonCreation(Network network, Person invitee, String template, Person inviter) {
		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile(new StringReader(template), "invitation-email");

		Map scopes = parseTemplateData(invitee, network, inviter);

		try {
			mustache.execute(writer, scopes);
			writer.flush();

			String emailBody = writer.toString();
			String subject = network.name + " - Convite enviado por " + inviter.getName();
			sendSimpleMail(invitee.getEmail(), subject, emailBody);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public String formatMessage(String name, String login, String password){
		String message = "Prezado(a) associado(a) {{name}},<br>" +
				"<br>" +
				"Temos o prazer de encaminhar um breve passo-a-passo para que possa fazer o download e usar o aplicativo de notícias da AMATRA-2, desenvolvido pela empresa <a href='https://xarx.co'>XARX</a> com curadoria de conteúdo da  <a href='http://satya.trix.rocks'>SATYA Comunicação e Tecnologia</a>. <br>" +
				"<br>" +
				"A plataforma está disponível nas lojas virtuais da Apple Store (para quem tem IPhone e sistema operacional iOS) e Play Store (para aparelhos com sistema Android).<br>" +
				"<br>" +
				" Se preferir acessar o conteúdo em um laptop, tablet ou qualquer outro tipo de computador, basta entrar no link http://satya.trix.rocks/, fornecer login e senha e, então, entrar na Estação AMATRA-2, exclusiva para associados e associadas da entidade.<br>" +
				"<br>" +
				"Login:	<b>{{login}}</b></p><br>" +
				"Senha:	<b>{{password}}</b><br>" +
				"<br>" +
				"<b>Para iOS (iPhone)</b><br>" +
				"1- Clique no link para download: https://itunes.apple.com/us/app/satya/id1095437609?ls=1&mt=8<br>" +
				"2- Após instalar, abra o app. Vai aparecer a página inicial de SATYA<br>" +
				"3- Clique do lado esquerdo (nas três barrinhas) e aparecerá um menu. Vá em entrar e digite seu login e senha<br>" +
				"4- Após se logar, na mesma barra de menu à esquerda vai estar a opção para acessar a Estação AMATRA-2<br>" +
				"5- Basta permanecer logado, que o app vai sempre abrir na AMATRA-2<br>" +
				"<br>" +
				"<p><b>Android (Samsung, Motorola e outros modelos Android)</b><br>" +
				"1- Clique no link para download: https://play.google.com/store/apps/details?id=co.xarx.satya<br>" +
				"2- Após instalar, abra o app. Vai aparecer a página inicial de SATYA<br>" +
				"3- Clique do lado esquerdo (nas três barrinhas) e aparecerá um menu. Vá em entrar e digite o login e senha abaixo:<br>" +
				"4- Após se logar, na mesma barra de menu à esquerda vai estar a opção para acessar a Estação AMATRA-2<br>" +
				"5- Basta permanecer logado, que o app vai sempre abrir na página da AMATRA-2<br>" +
				"<br>" +
				"Esperamos que o aplicativo seja útil. Boa navegação!<br>" +
				"<br>" +
				"<b>Diretoria AMATRA</b>-2<br>";

		return message
				.replace("{{name}}", name)
				.replace("{{login}}", login)
				.replace("{{password}}", password);
	}

	public Map parseTemplateData(Person invitee, Network network, Person inviter) {
		Color c1 = Color.decode(network.primaryColors.get("500"));
		Color c2 = Color.decode(network.secondaryColors.get("300"));
		Integer bgColor = Integer.parseInt(network.backgroundColors.get("500").replace("#", ""), 16);
		Integer referenceColor = Integer.parseInt("ffffff", 16);

		String networkNameColor = (bgColor > referenceColor / 2) ? "black" : "white";

		if (network.invitationMessage == null) network.invitationMessage = defaultMessage.replace("{{inviterName}}", inviter.getName()).replace("{{networkName}}", network.getName());

		HashMap<String, Object> scopes = new HashMap<>();
		scopes.put("networkName", network.name);
		scopes.put("primaryColor", "rgb(" + c1.getRed() + ", " + c1.getGreen() + ", " + c1.getBlue() + " )");
		scopes.put("secondaryColor", "rgb(" + c2.getRed() + ", " + c2.getGreen() + ", " + c2.getBlue() + " )");
		scopes.put("network", network);
		scopes.put("inviterName", inviter.getName());
		scopes.put("inviterEmail", inviter.getEmail());
		scopes.put("networkNameColor", networkNameColor);
		scopes.put("name", invitee.getName());
		scopes.put("plainPassword", invitee.user.password);
		scopes.put("link", "http://" + network.getRealDomain());

		return scopes;
	}

	public void createPerson(String email, String username, String name, String tenantId, List<Integer> stationIds){
		QPerson qp = QPerson.person;
		Person person = personRepository.findOne(qp.username.eq(username).and(qp.tenantId.eq(tenantId)));
		Person person1 = personRepository.findOne(qp.email.eq(email).and(qp.tenantId.eq(tenantId)));

		if(person1 != null){
			if(person1.user.password == null) {
				person1.user.password = StringUtil.generateRandomString(6, "a#");
			}

			for(Integer stationId: stationIds){
				if(!stationRepository.findByPersonId(person1.id).contains(stationRepository.findOne(stationId))){
					StationRole role = new StationRole();
					role.tenantId = tenantId;
					role.station = stationRepository.findOne(stationId);
					role.person = person1;
					stationRolesRepository.save(role);
				}
			}
			personRepository.save(person1);
			return;
		}

		if(person != null){
			for(int i = 1; ; i++){
				if(personRepository.findOne(qp.username.eq(username + String.valueOf(i))) != null){
					continue;
				}
				username += String.valueOf(i);
				break;
			}
		}

		person = new Person();
		person.email = email;
		person.name = name;
		person.username = username;
		person.tenantId = tenantId;
		person.password = StringUtil.generateRandomString(6, "a#");

		User user = new User();
		user.password = person.password;
		user.username = person.username;
		user.tenantId = tenantId;
		user.enabled = true;

		List<StationRole> stationRoles = new ArrayList<>();
		for(Integer stationId: stationIds){
			Station station = stationRepository.findOne(stationId);

			StationRole stationRole = new StationRole();
			stationRole.person = person;
			stationRole.station = station;
			stationRole.tenantId = tenantId;
			stationRoles.add(stationRole);

			UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.USER, station);
			authority.tenantId = tenantId;
			authority.user = user;
			user.addAuthority(authority);
		}

		person.user = user;
		personRepository.save(person);
		stationRolesRepository.save(stationRoles);
	}

	public void createPersonBatch(Map<String, String> invitees, String tenantId, List<Integer> stations){
		for(Map.Entry<String, String> invitee: invitees.entrySet()){
			createPerson(invitee.getKey(), invitee.getKey().split("@")[0], invitee.getValue(), tenantId, stations);
//			if(p != null) personRepository.save(p);
		}
	}

	public void batchInvitation(Map<String, String> invitees, String tenantId, String subject, List<Integer> stations){
		createPersonBatch(invitees, tenantId, stations);
		for(String email: invitees.keySet()){
			QPerson qp = QPerson.person;
			Person invitee = personRepository.findOne(QPerson.person.email.eq(email).and(qp.tenantId.eq(tenantId)));
			sendSimpleMail(invitee.getEmail(), subject,formatMessage(invitee.getName(), invitee.user.username, invitee.user.password));
		}
	}
}
