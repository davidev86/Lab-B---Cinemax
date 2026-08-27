/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server di conferma dell'avvenuta memorizzazione di una proiezione.
 * <p>
 * Incapsula l'identificatore univoco assegnato alla proiezione (chiave primaria generata dal database)
 * a seguito dell'elaborazione di un comando di creazione o aggiornamento.
 * </p>
 */
public class StoreProjectionResponse implements Response {
	
	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco della proiezione generato o memorizzato nel database.
	 */
	private Integer id;
	
	/**
	 * Costruttore predefinito senza argomenti.
	 
	 */
	public StoreProjectionResponse() {
	}
	
	/**
	 * Costruisce una risposta di memorizzazione specificando l'identificatore della proiezione.
	 *
	 * @param id l'identificatore univoco assegnato alla proiezione
	 */
	public StoreProjectionResponse(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce l'identificatore della proiezione memorizzata.
	 *
	 * @return l'identificatore numerico della proiezione
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore univoco della proiezione memorizzata.
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}