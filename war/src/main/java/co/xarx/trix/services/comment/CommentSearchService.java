package co.xarx.trix.services.comment;

import co.xarx.trix.api.v2.CommentData;
import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.page.query.statement.CommentStatement;
import co.xarx.trix.persistence.CommentRepository;
import co.xarx.trix.services.AbstractSearchService;
import co.xarx.trix.services.security.PermissionFilterService;
import co.xarx.trix.util.ImmutablePage;
import co.xarx.trix.util.SpringDataUtil;
import co.xarx.trix.web.rest.mapper.CommentDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentSearchService extends AbstractSearchService {

	private ESCommentService commentService;
	private CommentDataMapper commentDataMapper;
	private CommentRepository commentRepository;
	private PermissionFilterService permissionFilterService;

	@Autowired
	public CommentSearchService(ESCommentService commentService, CommentDataMapper commentDataMapper, CommentRepository commentRepository, PermissionFilterService permissionFilterService) {
		this.commentService = commentService;
		this.commentDataMapper = commentDataMapper;
		this.commentRepository = commentRepository;
		this.permissionFilterService = permissionFilterService;
	}

	public ImmutablePage<CommentData> search(CommentStatement params, Integer page, Integer size) {
		List<Integer> searchResults = searchIds(params);
		List<CommentData> result;
		if (searchResults.isEmpty())
			return new ImmutablePage<>(page);

		List<Integer> idsToGetFromDB = getPaginatedIds(searchResults, page, size);

		Sort sort = SpringDataUtil.getSort(params.getOrders());
		List<Comment> entities = idsToGetFromDB.size() > 0 ? commentRepository.findAllWithAuthors(idsToGetFromDB, sort)
		: new ArrayList<>();

		if (entities == null)
			return new ImmutablePage<>(page);

		result = entities.stream()
				.map(entity -> commentDataMapper.asDto(entity))
				.collect(Collectors.toList());

		return new ImmutablePage(result, page, searchResults.size());
	}

	private List<Integer> searchIds(CommentStatement params) {
		List<Integer> results = commentService.findIds(params);
		return permissionFilterService.filterIds(results, Comment.class, "read");
	}
}
