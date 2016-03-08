package co.xarx.trix.dto;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.StationRole;

import java.util.List;

public class PersonCreateDto extends Person {

	public List<StationRole> stationsRole;
	public Boolean emailNotification;
}
