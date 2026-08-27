/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server contenente i dettagli completi di una singola proiezione cinematografica.
 * <p>
 * Incapsula un'istanza di {@link ProjectionDetails} restituita a seguito dell'elaborazione
 * di una richiesta di ricerca proiezione per identificativo.
 * </p>
 */
public class GetProjectionResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Dettagli descrittivi ed operativi della proiezione incapsulati nella risposta.
	 */
	private ProjectionDetails projection;

	/**
	 * Costruttore predefinito senza argomenti.

	 */
	public GetProjectionResponse() {
	}

	/**
	 * Costruisce una risposta specificando i dettagli della proiezione.
	 *
	 * @param projection l'istanza di {@link ProjectionDetails} contenente le informazioni della proiezione
	 */
	public GetProjectionResponse(ProjectionDetails projection) {
		this.projection = projection;
	}

	/**
	 * Restituisce i dettagli della proiezione incapsulati nella risposta.
	 *
	 * @return l'oggetto {@link ProjectionDetails}
	 */
	public ProjectionDetails getProjection() {
		return projection;
	}

	/**
	 * Imposta i dettagli della proiezione da restituire al client.
	 *
	 * @param projection istanza di {@link ProjectionDetails}
	 */
	public void setProjections(ProjectionDetails projection) {
		this.projection = projection;
	}
}

