package test.integration;

import co.xarx.trix.config.DatabaseTestConfig;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.ApplicationTestConfig;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@Category(IntegrationTest.class)
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.xml.parsers.*", "org.apache.log4j.*", "org.xml.sax.*", "org.w3c.dom.*", "com.sun.xml.*", "javax.xml.stream.*", "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*", "org.slf4j.*"})
@PrepareForTest({TenantContextHolder.class})
@ActiveProfiles(profiles = "dev")
@Transactional
@ContextConfiguration(
		loader = AnnotationConfigContextLoader.class,
		classes = {ApplicationTestConfig.class, DatabaseTestConfig.class})
public abstract class AbstractIntegrationTest {


	@Before
	public final void integrationSetup() throws Exception {

		PowerMockito.mockStatic(TenantContextHolder.class);
		PowerMockito.when(TenantContextHolder.getCurrentTenantId()).thenReturn("test");
	}
}
