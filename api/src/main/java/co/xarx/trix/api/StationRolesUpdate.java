package co.xarx.trix.api;

import java.util.List;

/**
 * Created by misael on 1/19/2016.
 */
public class StationRolesUpdate {
	public boolean writer = false;
	public boolean editor = false;
	public boolean admin = false;
	public List<Integer> personsIds;
	public List<Integer> stationsIds;
}
