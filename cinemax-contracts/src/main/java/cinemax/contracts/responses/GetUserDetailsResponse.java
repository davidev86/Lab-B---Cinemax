/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.dto.UserDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta che incapsula i dettagli completi di un utente registrato nel sistema.
 */
public class GetUserDetailsResponse  implements Response  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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


