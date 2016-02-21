package co.xarx.trix.api;

import java.io.Serializable;
import java.util.List;

public class PersonData implements Serializable {
	private static final long serialVersionUID = 2078630211043976635L;
	public PersonDto person;
	public NetworkDto network;
	public List<StationDto> stations;
	public PersonPermissions personPermissions;
	public List<PostView> recent;
	public List<PostView> popular;

	public List<Integer> postsRead;
	public List<Integer> bookmarks;
	public List<Integer> recommends;

	public String publicCloudfrontUrl;
	public String privateCloudfrontUrl;
	public boolean noVisibleStation = false;

	public boolean noPassword = false;
}

