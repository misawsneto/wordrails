package co.xarx.trix.config.modelmapper;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.domain.Person;
import org.modelmapper.PropertyMap;

public class PersonDataMap extends PropertyMap<Person, PersonData> {

	@Override
	protected void configure() {
		map(source.getCover(), destination.getCover());
		map(source.getImage(), destination.getProfilePicture());
		map().setProfilePictureHash(source.getImageHash());
		map().setTwitter(source.getTwitterHandle());
	}
}
