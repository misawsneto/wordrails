package co.xarx.trix.api.v2;

import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"type"})
public class MobileStats implements Serializable {
//	public float averageRaiting;
//	public Integer downloads;
	public Integer currentInstallations;
	public Integer weeklyActiveUsers;
	public Integer monthlyActiveUsers;
	public Integer monthlyDownloads;
	public Constants.MobilePlatform type;

	public MobileStats(){
		currentInstallations = 0;
		weeklyActiveUsers = 0;
		monthlyActiveUsers = 0;
		monthlyDownloads = 0;
	}
}