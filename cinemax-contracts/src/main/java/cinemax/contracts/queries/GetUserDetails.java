/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.UserRequest;

/**
 * Query per il recupero dei dettagli anagrafici e del profilo di un utente specifico.
 
 */
public class GetUserDetails implements Query, UserRequest {
		
	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco dell'utente nel database.
	 */
	private Integer UserId;
	
	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetUserDetails() {
	}
	
	/**
	 * Costruisce un'istanza della query specificando l'identificatore dell'utente.
	 *
	 * @param userId l'identificatore univoco dell'utente richiesto
	 */
	public GetUserDetails(Integer userId) {
		this.UserId = userId;
	}

	/**
	 * Restituisce l'identificatore dell'utente target della ricerca.
	 *
	 * @return l'identificatore numerico dell'utente, oppure {@code null} se non impostato
	 */
	public Integer getUserId() {
		return UserId;
	}

	/**
	 * Imposta l'identificatore dell'utente target della ricerca.
	 *
	 * @param userId
	 */
	public void setUserId(Integer userId) {
		UserId = userId;
	}	
}

