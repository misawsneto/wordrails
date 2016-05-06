package co.xarx.trix.api.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationPermissionData {

	@Getter
	@AllArgsConstructor
	public class Permission {
		private String username;
		@JsonUnwrapped
		private PermissionData permissionData;
	}

	private Integer stationId;
	private List<Permission> userPermissions = new ArrayList<>();
}
