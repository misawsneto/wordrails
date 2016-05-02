package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.PageableQueryData;
import co.xarx.trix.domain.page.query.PageableQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {AbstractStatementMapper.class})
public interface PageableQueryMapper {
//	size,from,startShift,indexes,indexExceptions

	@Mappings({
			@Mapping(target = "tenantId", ignore = true),
			@Mapping(target = "createdAt", ignore = true),
			@Mapping(target = "updatedAt", ignore = true),
			@Mapping(target = "version", ignore = true),
			@Mapping(target = "size", ignore = true),
			@Mapping(target = "from", ignore = true),
			@Mapping(target = "startShift", ignore = true),
			@Mapping(target = "indexes", ignore = true),
			@Mapping(target = "indexExceptions", ignore = true),
			@Mapping(target = "objectStatement", source = "statement")
	})
	PageableQuery asEntity(PageableQueryData dto);
}
