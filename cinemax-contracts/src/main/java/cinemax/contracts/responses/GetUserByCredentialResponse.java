package cinemax.contracts.responses;

import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.interfaces.Response;

public class GetUserByCredentialResponse  implements Response  {
	
	public GetUserByCredentialResponse(UserMinInfo user) {
		this.user = user;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserMinInfo user;

	public UserMinInfo getUser() {
		return user;
	}

	public void setUser(UserMinInfo user) {
		this.user = user;
	}
	    
}