package cinemax.contracts.queries;

import cinemax.contracts.interfaces.*;

public class GetUserDetails implements Query, UserRequest  {
		
	private Integer UserId;
	
	public GetUserDetails() {
	}
	
	public GetUserDetails(Integer userId) {
		this.UserId = userId;
	}

	public Integer getUserId() {
		return UserId;
	}

	public void setUserId(Integer userId) {
		UserId = userId;
	}	
	
}
 