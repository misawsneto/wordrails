package co.xarx.trix.api;

import co.xarx.trix.api.v2.UserPermissionData;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PersonData implements Serializable {
	private static final long serialVersionUID = 2078630211043976635L;
	public PersonDto person;
	public NetworkDto network;
	public List<StationView> stations;
	public StationView defaultStation;
	public PersonPermissions personPermissions;
	public UserPermissionData permissions;
	public List<PostView> recent;
	public List<PostView> popular;

	public Date lastLogin;

	public String imageHash;
	public String coverHash;

	public List<Integer> postsRead;
	public List<Integer> bookmarks;
	public List<Integer> recommends;

	public List<MenuEntryDto> sections;
	public List<MenuEntryDto> menuEntries;

	public String publicCloudfrontUrl;
	public String privateCloudfrontUrl;
	public boolean noVisibleStation = false;

	public boolean isAdmin = false;
	public boolean noPassword = false;
}

