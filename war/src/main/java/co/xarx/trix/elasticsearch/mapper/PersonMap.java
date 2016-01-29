package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Person;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import org.modelmapper.PropertyMap;

public class PersonMap extends PropertyMap<Person, ESPerson> {

	@Override
	protected void configure() {
		map().setCover(source.getCover().originalHash);
		map().setProfilePicture(source.getImage().originalHash);
		map().setTwitter(source.getTwitterHandle());
	}
}
