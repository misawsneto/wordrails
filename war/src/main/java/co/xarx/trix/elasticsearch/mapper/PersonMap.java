package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.ESPerson;
import org.modelmapper.PropertyMap;

public class PersonMap extends PropertyMap<Person, ESPerson> {

	@Override
	protected void configure() {
		map().setCover(source.getCoverHash());
		map().setProfilePicture(source.getImage().getOriginalHash());
		map().setTwitter(source.getTwitterHandle());
	}
}
