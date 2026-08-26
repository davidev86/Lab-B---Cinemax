/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.UserRequest;

/**
 * Query per recuperare i dettagli completi di un utente specifico identificato tramite il suo ID univoco.
 */
public class GetUserDetails implements Query, UserRequest  {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
 


