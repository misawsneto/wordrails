//package co.xarx.trix.web.rest.mapper;
//
//import co.xarx.trix.api.v2.PersonData;
//import co.xarx.trix.domain.Person;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Mappings;
//import org.springframework.stereotype.Component;
//
//@Component
//@Mapper(componentModel = "spring")
//public interface PersonMapper {
//
//	@Mappings({
//			@Mapping(target = "tenantId", ignore = true),
//			@Mapping(target = "createdAt", ignore = true),
//			@Mapping(target = "updatedAt", ignore = true),
//			@Mapping(target = "version", ignore = true),
//			@Mapping(target = "size", ignore = true)
//	})
//	PersonData asDto(Person entity);
//}
