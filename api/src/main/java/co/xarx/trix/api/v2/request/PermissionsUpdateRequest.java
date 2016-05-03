package co.xarx.trix.api.v2.request;

import java.util.List;

public class PermissionsUpdateRequest {

	public boolean read = false;
	public boolean write = false;
	public boolean create = false;
	public boolean delete = false;
	public boolean administration = false;
	public boolean moderation = false;
	public List<Integer> stationsIds;
	public List<String> usernames;
}
