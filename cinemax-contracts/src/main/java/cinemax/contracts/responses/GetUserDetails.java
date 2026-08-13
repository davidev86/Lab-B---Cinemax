package cinemax.contracts.responses;

import cinemax.contracts.dto.UserDetails;
import cinemax.contracts.dto.UserMinInfos;
import cinemax.contracts.interfaces.Response;

public class GetUserDetailsResponse  implements Response  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2196281327073584312L;

	public GetUserDetailsResponse(UserDetails user) {
		this.user = user;
	}
	
	/**
	 * 
	 */
	
	private UserDetails user;

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}
	    
}