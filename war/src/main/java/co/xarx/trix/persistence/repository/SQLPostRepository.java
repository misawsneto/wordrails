package co.xarx.trix.persistence.repository;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.jooq.tables.Person;
import co.xarx.trix.jooq.tables.Post;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static co.xarx.trix.jooq.tables.Person.PERSON;
import static co.xarx.trix.jooq.tables.Post.POST;

@Repository
public class SQLPostRepository {

	private DSLContext dsl;

	@Autowired
	public SQLPostRepository(DSLContext dslContext) {
		this.dsl = dslContext;
	}

	public List<PostData> findByIds(List<Integer> ids) {
		Post post = POST.as("post");
		Person author = PERSON.as("author");

		String json = dsl.select()
				.from(post)
				.join(author).on(post.AUTHOR_ID.eq(author.ID))
				.where(post.ID.in(82))
				.fetch().formatJSON();

		List<PostData> fetch = dsl.select()
				.from(post)
				.join(author).on(post.AUTHOR_ID.eq(author.ID))
				.where(post.ID.in(ids))
				.fetchInto(PostData.class);

		return fetch;
	}
}
