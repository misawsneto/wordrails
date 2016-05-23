package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PersonDataMapper {

	PersonDataMapper INSTANCE = Mappers.getMapper(PersonDataMapper.class);

	@Mappings({
			@Mapping(target = "id", source = "id"),
			@Mapping(target = "name", source = "name"),
			@Mapping(target = "username", source = "username"),
			@Mapping(target = "email", source = "email"),
			@Mapping(target = "twitter", source = "twitterHandle"),
			@Mapping(target = "coverHash", source = "coverHash"),
			@Mapping(target = "profilePictureHash", source = "imageHash")
	})
	PersonData asDto(Person entity);
}
