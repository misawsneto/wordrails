package com.wordrails.api;

import java.io.Serializable;
import java.util.List;

public class PersonPermissions implements Serializable {
	private static final long serialVersionUID = 20595239705254680L;

	public List<StationPermission> stationPermissions;
	public NetworkPermission networkPermission;

	//Person
	public Integer personId;
	public String personName;
	public String username;
}