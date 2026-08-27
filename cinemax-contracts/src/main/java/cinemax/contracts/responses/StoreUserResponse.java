/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server di conferma dell'avvenuta memorizzazione o registrazione di un utente.
 * <p>
 * Incapsula l'identificatore univoco assegnato all'utente (chiave primaria generata dal database)
 * a seguito dell'elaborazione di una richiesta di inserimento o registrazione nel sistema.
 * </p>
 */
public class StoreUserResponse implements Response {
	
	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco dell'utente generato o memorizzato nel database.
	 */
	private Integer id;
	
	/**
	 * Costruttore predefinito senza argomenti.

	 */
	public StoreUserResponse() {
	}
	
	/**
	 * Costruisce una risposta di memorizzazione specificando l'identificatore dell'utente.
	 *
	 * @param id l'identificatore univoco assegnato all'utente registrato
	 */
	public StoreUserResponse(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce l'identificatore dell'utente registrato.
	 *
	 * @return l'identificatore dell'utente
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore univoco dell'utente registrato.
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}