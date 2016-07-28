package co.xarx.trix.services;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSearchService {

	protected List<Integer> getPaginatedIds(List<Integer> ids, Integer page, Integer size) {
		int from = size * page;
		if(ids.size() < size)
			size = ids.size();
		if(from < ids.size())
			return ids.subList(from, from + size <= ids.size() ? from + size : ids.size());
		else
			return new ArrayList<>();
	}
}
