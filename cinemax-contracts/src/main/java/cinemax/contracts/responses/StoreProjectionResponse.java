/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
/**
 * Risposta di conferma per l'archiviazione di una proiezione cinematografica, contenente l'ID univoco assegnato dal server.
 */
 
public class StoreProjectionResponse  implements Response  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	
	public StoreProjectionResponse() {
		
	}
	
	public StoreProjectionResponse(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}


