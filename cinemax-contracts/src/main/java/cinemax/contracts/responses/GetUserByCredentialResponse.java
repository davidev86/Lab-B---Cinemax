/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta che incapsula le informazioni minime di un utente ottenute dal processo di autenticazione con credenziali.
 */
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


