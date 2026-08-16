package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

public class StoreUserResponse  implements Response  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	
	public StoreUserResponse(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
