package co.xarx.trix.api.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsData implements Serializable {

	public List<Integer> generalStatsJson;
	public TreeMap<Long, ReadsCommentsRecommendsCountData> dateStatsJson;
	public MobileStats iosStore;
	public MobileStats androidStore;
	public Map<String, Integer> fileSpace;
	public Map<String, Integer> stats; // atempt to remove generalStatsJson from this class

	public StatsData(){
		this.stats = new HashMap<>();
	}
}
