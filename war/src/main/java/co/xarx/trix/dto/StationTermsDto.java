package co.xarx.trix.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by misael on 12/08/2015.
 */
public class StationTermsDto implements Serializable{

    public Integer stationId;
    public Set<Integer> termIds;
}
