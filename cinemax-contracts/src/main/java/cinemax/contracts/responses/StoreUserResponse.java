/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta che comunica l'avvenuta registrazione di un nuovo utente fornendo l'ID generato dal database.
 */
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


