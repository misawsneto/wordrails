package co.xarx.trix.api.v2.request;

import co.xarx.trix.domain.Post;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveSectionsRequest {


	private List<Section> sections = new ArrayList<>();

	@Getter @Setter
	public abstract class Section {

		private String title;
		private String type;
		private String style;
		private Map<String, String> properties = new HashMap<>();
		private String orientation;
		private Integer topMargin;
		private Integer leftMargin;
		private Integer bottomMargin;
		private Integer rightMargin;
		private Integer topPadding;
		private Integer leftPadding;
		private Integer bottomPadding;
		private Integer rightPadding;

		public abstract String getType();
	}

	@Getter
	public class ContainerSection extends Section {

		Integer count = 0;
		private Map<Integer, Section> children;

		public void add(Section section) {
			if(children == null)
				children = new LinkedHashMap<>();

			children.put(count++, section);
		}

		@Override
		public String getType() {
			return Constants.Section.CONTAINER;
		}
	}

	@Getter @Setter
	public class QueryableSection extends Section {

		private Integer size;
		private List<FixedQuery> fixedQueries = new ArrayList<>();
		private PageableQuery pageableQuery;

		@Override
		public String getType() {
			return Constants.Section.QUERYABLE_LIST;
		}
	}

	public interface Query {
		Statement getStatement();
	}

	@Getter @Setter
	public class FixedQuery implements Query {
		private List<Integer> indexes = new ArrayList<>();
		private Statement statement;
	}

	@Getter @Setter
	public class PageableQuery implements Query {
		private Statement statement;
	}

	public interface Statement {
		Class getType();
	}

	public interface SortedStatement extends Statement {

		void addSort(String attribute, Boolean asc);

		Map<String, Boolean> getSorts();
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class PostStatement extends AbstractStatement implements Statement {

		private String query;
		private List<Integer> authors = new ArrayList<>();
		private List<Integer> stations = new ArrayList<>();
		private String state;
		private String from;
		private String until;
		private List<Integer> categories = new ArrayList<>();
		private List<String> tags = new ArrayList<>();
		private List<String> orders = new ArrayList<>();

		@Override
		public Class getType() {
			return Post.class;
		}
	}

	@Getter
	@Setter
	public abstract class AbstractStatement implements  SortedStatement {

		private Map<String, Boolean> sorts;
		private Set<Serializable> exceptionIds;

		public AbstractStatement() {
			sorts = new HashMap<>();
			exceptionIds = new HashSet<>();
		}

		@Override
		public void addSort(String attribute, Boolean asc) {
			sorts.put(attribute, asc);
		}
	}

}
