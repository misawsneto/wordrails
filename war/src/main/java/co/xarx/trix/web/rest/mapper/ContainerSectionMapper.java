package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.ContainerSectionData;
import co.xarx.trix.domain.page.ContainerSection;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {AbstractSectionMapper.class})
public interface ContainerSectionMapper {

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
			@Mapping(target = "children", source = "children")
	})
	ContainerSection asEntity(ContainerSectionData dto);

	@InheritConfiguration(name = "asEntity")
	void updateEntity(ContainerSectionData dto, @MappingTarget ContainerSection entity);
}
