package com.wordrails.util;

import com.wordrails.domain.Person;
import com.wordrails.domain.StationRole;

public class PersonCreateDto extends Person{

	public StationRole stationRole;
	public Boolean emailNotification;
}
