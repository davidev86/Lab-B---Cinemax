/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per la richiesta dei dettagli di una specifica proiezione tramite il suo identificatore.
 * <p>
 * Incapsula l'identificativo univoco della proiezione inviato dal client al server 
 * per estrarre le relative informazioni di programmazione (data e ora, film, sala, costi e posti).
 * </p>
 */
public class GetProjectionById implements Query, ProjectionRequest {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco della proiezione da ricercare nel database.
	 */
	private Integer idProiezione;	
	
	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetProjectionById() {
	}

	/**
	 * Crea un'istanza della query specificando l'identificatore della proiezione.
	 *
	 * @param idProiezione
	 */
	public GetProjectionById(Integer idProiezione) {
		super();
		this.setIdProiezione(idProiezione);
	}

	/**
	 * Restituisce l'identificatore della proiezione target della ricerca.
	 *
	 * @return l'identificatore numerico della proiezione, oppure {@code null} se non impostato
	 */
	public Integer getIdProiezione() {
		return idProiezione;
	}

	/**
	 * Imposta l'identificatore della proiezione target della ricerca.
	 *
	 * @param idProiezione
	 */
	public void setIdProiezione(Integer idProiezione) {
		this.idProiezione = idProiezione;
	}
} 

