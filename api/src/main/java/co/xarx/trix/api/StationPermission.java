package co.xarx.trix.api;

import java.io.Serializable;

public class StationPermission implements Serializable {
	private static final long serialVersionUID = -870078497247617149L;

	//Station
	public Integer stationId;
	public String stationName;
	public boolean writable;
	public boolean main;
	public String visibility;
	public Integer defaultPerspectiveId;

	// flags
	public boolean sponsored;
	public boolean social;
	public boolean topper;
	public boolean subheading;
	public boolean allowComments;
	public boolean allowSignup;
	public boolean allowSocialLogin;
	public boolean allowSocialShare;

	//Person
	public boolean admin;
	public boolean editor;
	public boolean moderator;
	public boolean writer;
	public boolean creator;
	public boolean sponsor;
}