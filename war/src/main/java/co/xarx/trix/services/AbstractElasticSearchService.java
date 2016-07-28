package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;

public class AbstractElasticSearchService {
	protected List<FieldSortBuilder> getSorts(List<String> sort) {
		List<FieldSortBuilder> sorts = new ArrayList<>();
		if (sort != null) {
			for (String s : sort) {
				SortOrder d = SortOrder.ASC;

				if (s.charAt(0) == '-') {
					d = SortOrder.DESC;
					s = s.substring(1, s.length());
				}

				sorts.add(new FieldSortBuilder(s).order(d));
			}
		}
		return sorts;
	}

	protected void applyShouldFilter(BoolFilterBuilder f, List terms, String termName) {
		if(terms != null && !terms.isEmpty()) {
			BoolFilterBuilder boolFilter = boolFilter();
			for (Object term : terms) {
				boolFilter.should(termFilter(termName, term));
			}
			f.must(boolFilter);
		}
	}

	protected void applyTenantFilter(BoolFilterBuilder q) {
		q.must(termFilter("tenantId", TenantContextHolder.getCurrentTenantId().toLowerCase()));
	}

	protected void applyDateFilter(BoolFilterBuilder f, String from, String until, String field) {
		if(from != null || until != null) {
			RangeFilterBuilder dateFilter = rangeFilter(field);

			if(from != null)
				dateFilter.from(from);
			if(until != null)
				dateFilter.to(until);

			f.must(dateFilter);
		}
	}

	protected void applyTypeFilter(BoolFilterBuilder f, String type) {
		if (type != null && !type.isEmpty()) {
			f.must(termFilter("_type", type.toLowerCase()));
		}
	}
}
