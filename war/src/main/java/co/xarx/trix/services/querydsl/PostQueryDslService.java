package co.xarx.trix.services.querydsl;

import co.xarx.trix.domain.QPost;

public class PostQueryDslService extends AbstractQueryDslService {




	@Override
	public QPost getEntityPathBase() {
		return QPost.post1;
	}
}
