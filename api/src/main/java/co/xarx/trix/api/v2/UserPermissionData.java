package co.xarx.trix.api.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPermissionData {

	@Getter
	@AllArgsConstructor
	public class Permission {
		private Integer stationId;
		@JsonUnwrapped
		private PermissionData permissionData;
	}

	@Getter
	private List<Permission> stationPermissions = new ArrayList<>();
}
