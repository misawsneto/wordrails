package co.xarx.trix.services;

import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.services.auth.AuthService;
import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {

	private StatisticsService statisticsService;
	private Client client;
	private AuthService authService;

	@Before
	public void setUp(){
//		client = mock(Client.class);
//		authService = mock(AuthService.class);
//		statisticsService = new StatisticsService(client, authService);
	}

	@Test
	public void testGeneralCounter(){

	}
}
