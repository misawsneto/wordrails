package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.ESComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ESCommentMapper extends ESMapper<ESComment, Comment> {

	@Mappings({
			@Mapping(target = "id", source = "id"),
			@Mapping(target = "body", source = "body"),
			@Mapping(target = "date", source = "date"),
			@Mapping(target = "postId", source = "post.id"),
			@Mapping(target = "authorId", source = "author.id"),
			@Mapping(target = "stationId", source = "post.station.id")
	})
	ESComment asDto(Comment entity);
}
