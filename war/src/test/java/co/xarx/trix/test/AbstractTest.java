package co.xarx.trix.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "dev")
@ContextConfiguration({
		"classpath:applicationContext-test.xml",
		"classpath:applicationContext-quartz.xml",
		"classpath:applicationContext-integration.xml",
		"classpath:applicationContext-security.xml"
})
public abstract class AbstractTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	protected ApplicationContext context;
//	private @Autowired  ObjectMapper objectMapper;
	//private @Autowired StationRepository stationRepository;

	@Test
	public void doTest() {
//		Station station = stationRepository.findOne(1);
//		System.out.println(station.stationPerspectives.toString());
	}
}