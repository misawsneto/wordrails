package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Person;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import org.modelmapper.PropertyMap;

public class PersonMap extends PropertyMap<Person, ESPerson> {

	@Override
	protected void configure() {
		map().setCover(source.getCover().hashs);
		map().setTenantId(source.getTenantId());
		map().setProfilePicture(source.getImage().hashs);
		map().setTwitter(source.getTwitterHandle());
	}
}
