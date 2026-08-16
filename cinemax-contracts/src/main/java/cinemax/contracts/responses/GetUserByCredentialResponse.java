package cinemax.contracts.responses;

import cinemax.contracts.dto.UserMinInfos;
import cinemax.contracts.interfaces.Response;

public class GetUserByCredentialResponse  implements Response  {
	
	public GetUserByCredentialResponse(UserMinInfos user) {
		this.user = user;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserMinInfos user;

	public UserMinInfos getUser() {
		return user;
	}

	public void setUser(UserMinInfos user) {
		this.user = user;
	}
	    
}