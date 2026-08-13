package cinemax.contracts.queries;

import cinemax.contracts.interfaces.*;

public class GetUserDetails implements Query, UserRequest  {

	private Integer UserId;

	public Integer getUserId() {
		return UserId;
	}

	public void setUserId(Integer userId) {
		UserId = userId;
	}	
	
}
 