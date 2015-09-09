package com.wordrails;

public class PermissionId {

	public String baseUrl;
	public Integer personId;
	public Integer networkId;
	
	@Override
	public boolean equals(Object obj) {
		PermissionId id = (PermissionId)obj;
		if(baseUrl!=null&&personId!=null&&networkId!=null)
			return baseUrl.equals(id.baseUrl) && personId.equals(id.personId) && networkId.equals(id.networkId);
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		if(baseUrl!=null&&personId!=null&&networkId!=null)
			return baseUrl.hashCode() * 31 + personId + networkId;
		else
			return 0;
	}
}
