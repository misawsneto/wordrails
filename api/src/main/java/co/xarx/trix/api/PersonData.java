package co.xarx.trix.api;

import co.xarx.trix.api.v2.UserPermissionData;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PersonData implements Serializable {
	private static final long serialVersionUID = 2078630211043976635L;
	public PersonDto person;
	public NetworkData network;
	public List<StationView> stations;
	public StationView defaultStation;
	public PersonPermissions personPermissions;
	public UserPermissionData permissions;
	public TermPerspectiveView perspective;

	public Date lastLogin;

	public String imageHash;
	public String coverHash;

	public List<Integer> bookmarks;
	public List<Integer> recommends;

	public List<StationPostDto> bookmarkedPosts;
	public List<StationPostDto> recommendPosts;

	public List<MenuEntryDto> sections;
	public List<MenuEntryDto> menuEntries;

	public String publicCloudfrontUrl;
	public String privateCloudfrontUrl;
	public boolean noVisibleStation = false;

	public boolean isAdmin = false;
	public boolean noPassword = false;
	public List<Integer> followingStations;
}