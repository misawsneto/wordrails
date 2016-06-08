package co.xarx.trix.persistence;

import co.xarx.trix.IntegrationTest;
import co.xarx.trix.TestArtifactsFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class StationRepositorySecurityTest extends IntegrationTest {

	@Autowired
	private StationRepository stationRepository;

	@Ignore
	@Test(expected = AccessDeniedException.class)
	public void accessDeniedOnSaveStationWithAnonymousUser() throws Exception {
		stationRepository.save(TestArtifactsFactory.createStation());
	}
}