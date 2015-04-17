package com.wordrails.api;

import java.io.Serializable;
import java.util.List;

public class PersonData implements Serializable{
	private static final long serialVersionUID = 2078630211043976635L;
	public PersonDto person;
	public NetworkDto network;
	public NetworkRoleDto networkRole;
	public List<StationDto> stations;
	public PersonPermissions personPermissions;
}

