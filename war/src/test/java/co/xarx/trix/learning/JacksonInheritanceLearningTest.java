package co.xarx.trix.learning;

import co.xarx.trix.domain.page.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by misael on 17/03/2016.
 */
public class JacksonInheritanceLearningTest {

	ObjectMapper objectMapper;

	@Before
	public void setUp() throws Exception {
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testSerializing() throws Exception {
		String page  = "{\"title\": \"Título\", " +
				"\"sections\":{" +
					"\"1\": {\"title\": \"Page 1\", \"sectionType\":\"QueryableListSection\"}," +
					"\"2\": {\"title\": \"Page 2\", \"sectionType\":\"QueryableListSection\"}," +
					"\"3\": {\"title\": \"Page 3\", \"sectionType\":\"QueryableListSection\"}" +
				"}}";

		objectMapper.readValue(page, Page.class);
	}
}
