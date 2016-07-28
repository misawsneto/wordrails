package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.QueryableSectionData;
import co.xarx.trix.domain.page.QueryableListSection;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
		uses = {
				AbstractSectionMapper.class,
				FixedQueryMapper.class,
				PageableQueryMapper.class
		}
)
public interface QueryableSectionMapper {

	@Mappings({
			@Mapping(target = "tenantId", ignore = true),
			@Mapping(target = "createdAt", ignore = true),
			@Mapping(target = "updatedAt", ignore = true),
			@Mapping(target = "version", ignore = true),
			@Mapping(target = "margin", ignore = true),
			@Mapping(target = "padding", ignore = true),
			@Mapping(target = "blocks", ignore = true),
			@Mapping(target = "page", ignore = true),
			@Mapping(target = "topMargin", source = "topMargin"),
			@Mapping(target = "leftMargin", source = "leftMargin"),
			@Mapping(target = "bottomMargin", source = "bottomMargin"),
			@Mapping(target = "rightMargin", source = "rightMargin"),
			@Mapping(target = "topPadding", source = "topPadding"),
			@Mapping(target = "leftPadding", source = "leftPadding"),
			@Mapping(target = "bottomPadding", source = "bottomPadding"),
			@Mapping(target = "rightPadding", source = "rightPadding"),
			@Mapping(target = "orderPosition", source = "orderPosition"),
			@Mapping(target = "pctSize", source = "pctSize"),
			@Mapping(target = "pageable", source = "pageable"),
			@Mapping(target = "size", source = "size"),
			@Mapping(target = "fixedQueries", source = "fixedQueries"),
			@Mapping(target = "pageableQuery", source = "pageableQuery")
	})
	QueryableListSection asEntity(QueryableSectionData dto);

	@InheritConfiguration(name = "asEntity")
	void updateEntity(QueryableSectionData dto, @MappingTarget QueryableListSection entity);
}
