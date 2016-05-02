package co.xarx.trix.services;

import co.xarx.trix.domain.Network;
import org.junit.Before;
import org.junit.Test;

public class EmailServiceTest {

	private EmailService emailService;
	private Network network;

	@Before
	public void setUp() throws Exception {
		emailService = new EmailService();
		network = new Network();
	}

	@Test
	public void testSendNetworkInvitation() throws Exception {
	}
}