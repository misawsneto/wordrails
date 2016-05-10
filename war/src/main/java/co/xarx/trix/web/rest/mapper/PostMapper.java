//package co.xarx.trix.web.rest.mapper;
//
//import co.xarx.trix.api.v2.PostData;
//import co.xarx.trix.domain.Post;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Mappings;
//import org.springframework.stereotype.Component;
//
//@Component
//@Mapper(componentModel = "spring", uses = {PersonMapper.class})
//public interface PostMapper {
//
//	@Mappings({
//			@Mapping(target = "tenantId", ignore = true),
//			@Mapping(target = "createdAt", ignore = true),
//			@Mapping(target = "updatedAt", ignore = true),
//			@Mapping(target = "version", ignore = true),
//			@Mapping(target = "size", ignore = true)
//	})
//	PostData asDto(Post entity);
//}
