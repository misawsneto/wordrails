package co.xarx.trix.services;

import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.NetworkCreate;
import co.xarx.trix.persistence.NetworkCreateRepository;
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
	private NetworkCreateRepository networkCreateRepository;

	@Before
	public void setUp() throws Exception {
		networkRepository = mock(NetworkRepository.class);
		networkCreateRepository = mock(NetworkCreateRepository.class);

		List<Object[]> networkList = new ArrayList<>();
		Object[] network1 = new Object[3];
		network1[0] = 1;
		network1[1] = "xarx.portodigital.org";
		network1[2] = "pd";
		networkList.add(network1);
		Object[] network2 = new Object[3];
		network2[0] = 2;
		network2[1] = "tupy.com";
		network2[2] = "demo";
		networkList.add(network2);

		when(networkRepository.findIdsAndDomain()).thenReturn(networkList);

		networkService = new NetworkService(networkRepository, networkCreateRepository);
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