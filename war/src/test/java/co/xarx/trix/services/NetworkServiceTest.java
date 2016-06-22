package co.xarx.trix.services;

import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkServiceTest {

	private NetworkService networkService;
	private NetworkRepository networkRepository;
	private EmailService emailService;

	@Before
	public void setUp() throws Exception {
		networkRepository = mock(NetworkRepository.class);

		List<Network> networkList = new ArrayList<>();
		Network network1 = new Network();
		network1.id = 1;
		network1.domain = "xarx.portodigital.org";
		network1.tenantId = "pd";
		networkList.add(network1);
		Network network2 = new Network();
		network2.id = 2;
		network2.domain = "tupy.com";
		network2.tenantId = "demo";
		networkList.add(network2);

		when(networkRepository.findAll()).thenReturn(networkList);

		networkService = new NetworkService(networkRepository, emailService);
	}

	@Test
	public void testDomain() throws Exception {
		String tenantId = networkService.getTenantFromHost("xarx.portodigital.org");
		String tenantId2 = networkService.getTenantFromHost("pd.trix.rocks");
		assertEquals(tenantId, "pd");
		assertEquals(tenantId2, "pd");
	}

	@Test
	public void testSubdomain() throws Exception {
		String tenantId = networkService.getTenantFromHost("demo.trix.rocks");
		String tenantId2 = networkService.getTenantFromHost("tupy.trix.rocks");
		String tenantId3 = networkService.getTenantFromHost("tupy.com");
		assertEquals(tenantId, "demo");
		assertEquals(tenantId2, null);
		assertEquals(tenantId3, "demo");
	}

	@Test
	public void testAddTenant() throws Exception {
		Network network = new Network();
		network.id = 3;
		network.tenantId = "cesar";

		when(networkRepository.findByTenantId(anyString())).thenReturn(null).thenReturn(network).thenThrow(new RuntimeException());

		String tenantId = networkService.getTenantFromHost("cesar.trix.rocks");
		String tenantId2 = networkService.getTenantFromHost("cesar.trix.rocks");
		String tenantId3 = networkService.getTenantFromHost("cesar.trix.rocks");

		assertEquals(tenantId, null);
		assertEquals(tenantId2, "cesar");
		assertEquals(tenantId3, "cesar");
	}
}
