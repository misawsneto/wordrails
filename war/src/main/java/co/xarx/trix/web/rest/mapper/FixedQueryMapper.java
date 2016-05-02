package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.FixedQueryData;
import co.xarx.trix.domain.page.query.FixedQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {AbstractStatementMapper.class})
public interface FixedQueryMapper {


	@Mappings({
			@Mapping(target = "tenantId", ignore = true),
			@Mapping(target = "createdAt", ignore = true),
			@Mapping(target = "updatedAt", ignore = true),
			@Mapping(target = "version", ignore = true),
			@Mapping(target = "objectStatement", source = "statement")
	})
	FixedQuery asEntity(FixedQueryData dto);
}
