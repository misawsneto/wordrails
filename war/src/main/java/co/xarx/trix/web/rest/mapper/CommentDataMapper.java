package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.CommentData;
import co.xarx.trix.domain.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {PersonDataMapper.class})
public interface CommentDataMapper {

	CommentDataMapper INSTANCE = Mappers.getMapper(CommentDataMapper.class);

	@Mappings({
			@Mapping(target = "id", source = "id"),
			@Mapping(target = "body", source = "body"),
			@Mapping(target = "date", source = "date"),
			@Mapping(target = "postId", source = "post.id"),
			@Mapping(target = "authorId", source = "author.id"),
			@Mapping(target = "stationId", source = "post.station.id"),
			@Mapping(target = "author", source = "author")
	})
	CommentData asDto(Comment entity);
}
