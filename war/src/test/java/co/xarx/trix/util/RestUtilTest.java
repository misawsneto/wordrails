package co.xarx.trix.util;

import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class RestUtilTest {

	@Test
	public void testPageable() throws Exception {
		Integer page = 1;
		Integer size = 10;
		List<String> orders = new ArrayList<>();
		orders.add("attribute1");
		orders.add("-attribute2");

		Pageable pageable = RestUtil.getPageable(page, size, orders);

		assertEquals(pageable.getPageNumber(), 1);
		assertEquals(pageable.getPageSize(), 10);
		assertEquals(pageable.getSort().getOrderFor("attribute1"), new Sort.Order(Sort.Direction.ASC, "attribute1"));
		assertEquals(pageable.getSort().getOrderFor("attribute2"), new Sort.Order(Sort.Direction.DESC, "attribute2"));
	}
}