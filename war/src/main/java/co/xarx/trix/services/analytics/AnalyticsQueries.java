package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.CommentData;
import co.xarx.trix.domain.AnalyticsEntity;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.page.query.statement.CommentStatement;
import co.xarx.trix.services.comment.CommentSearchService;
import co.xarx.trix.util.ImmutablePage;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsQueries {

	private CommentSearchService commentSearchService;
	private Map<Class<?>, String> commentSearchFields;
	private Map<Class<?>, String> recommendSearchFields;

	@Autowired
	public AnalyticsQueries(CommentSearchService commentSearchService){
		this.commentSearchService = commentSearchService;
		this.commentSearchFields = loadCommentStatementField();
	}

	private Map loadCommentStatementField() {
		Map fields = new HashMap<Class<?>, String>();

		fields.put(Post.class, "posts");
		fields.put(Person.class, "authors");
		fields.put(Station.class, "stations");

		return fields;
	}

	public Integer countRecommendsByEntity(AnalyticsEntity entity){
		return null;
	}

	public Map getCommentsByEntity(AnalyticsEntity entity){
		CommentStatement params = buildStatement(entity);
		ImmutablePage<CommentData> comments = commentSearchService.search(params, null, null);

		Map<Long, Long> hist = new HashMap();
		for(CommentData c: comments){
			long date = c.getDate().getTime();

			if(hist.get(date) == null){
				hist.put(date, 1L);
			} else {
				long i = hist.get(date);
				hist.put(date, i + 1);
			}
		}

		return hist;
	}

	public Integer countCommentsByEntity(AnalyticsEntity entity){
		CommentStatement params = buildStatement(entity);
		return commentSearchService.search(params, null, null).size();
	}

	public CommentStatement buildStatement(AnalyticsEntity entity){
		List<Integer> entityId = Lists.newArrayList(entity.getId());
		Class<?> clazz = CommentStatement.class.getClass();

		String fieldName = commentSearchFields.get(entity.getClass());
		Field field = null;

		try {
			field = clazz.getField(fieldName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		field.setAccessible(true);

		CommentStatement params = new CommentStatement();

		try {
			field.set(params, entityId);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return params;
	}
}

