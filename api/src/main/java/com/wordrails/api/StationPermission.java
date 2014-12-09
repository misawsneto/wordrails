package com.wordrails.api;

import java.io.Serializable;

public class StationPermission implements Serializable {
	private static final long serialVersionUID = -870078497247617149L;
	
	//Station
	public Integer stationId;
	public String stationName;
	public boolean writable;
	public boolean main;
	public String visibility;

	//Person
	public boolean admin;
	public boolean editor;
	public boolean writer;
}