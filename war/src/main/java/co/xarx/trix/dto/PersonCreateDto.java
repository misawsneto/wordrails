package co.xarx.trix.dto;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.StationRole;

public class PersonCreateDto extends Person {

	public StationRole stationRole;
	public Boolean emailNotification;
	public String invitation;
}
