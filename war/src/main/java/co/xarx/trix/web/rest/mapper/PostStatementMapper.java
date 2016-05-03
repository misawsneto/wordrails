package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.PostStatementData;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PostStatementMapper {

	PostStatementMapper INSTANCE = Mappers.getMapper(PostStatementMapper.class);

	@Mappings({
			@Mapping(target = "tenantId", ignore = true),
			@Mapping(target = "createdAt", ignore = true),
			@Mapping(target = "updatedAt", ignore = true),
			@Mapping(target = "version", ignore = true)
	})
	PostStatement asEntity(PostStatementData dto);
}
