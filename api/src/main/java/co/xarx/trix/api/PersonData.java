package co.xarx.trix.api;

import java.io.Serializable;
import java.util.List;

public class PersonData implements Serializable {
	private static final long serialVersionUID = 2078630211043976635L;
	public PersonDto person;
	public NetworkDto network;
	public List<StationDto> stations;
	public StationDto defaultStation;
	public PersonPermissions personPermissions;
	public List<PostView> recent;
	public List<PostView> popular;

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

