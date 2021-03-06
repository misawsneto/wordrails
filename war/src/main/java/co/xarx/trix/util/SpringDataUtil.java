package co.xarx.trix.util;

import org.springframework.data.domain.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class SpringDataUtil {

	public static <T> Page<T> getPageData(ImmutablePage<T> page, List<String> order) {
		if(page == null || page.isEmpty()) {
			return new PageImpl<>(new ArrayList<>());
		}

		return new PageImpl<>(page.items(), getPageable(page.getIndex(), page.size(), order), page.totalSize());
	}


	public static Pageable getPageable(Integer page, Integer size, List<String> order) {
		Assert.notNull(page, "Page must not be null");
		Assert.notNull(size, "Size must not be null");

		Sort sort = getSort(order);

		return new PageRequest(page, size, sort);
	}

	public static Sort getSort(List<String> order) {
		Sort sort = null;

		if (order != null && !order.isEmpty()) {
			List<Sort.Order> orders = new ArrayList<>();
			for (String s : order) {
				Sort.Direction d = Sort.Direction.ASC;

				if (s.charAt(0) == '-') {
					d = Sort.Direction.DESC;
					s = s.substring(1, s.length());
				}
				orders.add(new Sort.Order(d, s));
			}

			sort = new Sort(orders);
		}

		return sort;
	}
}