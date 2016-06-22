package co.xarx.trix.services;

import co.xarx.trix.domain.Invitation;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import org.junit.Before;
import org.junit.Test;

public class EmailServiceTest {

	private EmailService emailService;
	private Network network;
	private Invitation invitation;
	private Person person;

	@Before
	public void setUp() throws Exception {
		emailService = new EmailService();
		network = new Network();

		network.mainColor = "#FFFFFF";
		network.navbarColor = "#3594FF";
		network.name = "DEMO";
		network.domain = "trix.rocks";
		network.invitationMessage = "{{inviterName}} convidou-lhe para participar da rede <strong class=\"uc\"\n" + "style=\"text-transform: uppercase;\">{{networkName}}</strong>.";

		person = new Person();
		person.email = "test@test.com";
		person.name = "admin";
	}

	@Test
	public void testSendNetworkInvitation() throws Exception {
		Invitation invitation = new Invitation(network.getRealDomain());
		invitation.person = new Person();
		invitation.person.email = "jonas.agx@gmail.com";
		invitation.person.name = "Jonas";
		//Now it does nothing
//		emailService.validatePersonCreation(network, invitation, person);
	}
}