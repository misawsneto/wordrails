package co.xarx.trix.api.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreStatsData implements Serializable{
//	public float averageRaiting;
//	public Integer downloads;
	public Integer currentInstallations;
	public Integer weeklyActiveUsers;
	public Integer monthlyActiveUsers;
	public Integer monthlyDownloads;
}