package co.xarx.trix.api;

import java.util.List;

public class StationRolesUpdate {
	public boolean colaborator = false;
	public boolean writer = false;
	public boolean editor = false;
	public boolean admin = false;
	public boolean sponsor = false;
	public List<Integer> personsIds;
	public List<Integer> stationsIds;
	public List<String> usernames;
}
