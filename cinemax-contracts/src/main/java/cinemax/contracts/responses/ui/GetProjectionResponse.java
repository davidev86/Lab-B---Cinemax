/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses.ui;

import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server contenente la vista dettagliata di una singola proiezione cinematografica.
 * <p>
 * Incapsula un oggetto {@link ProjectionDetailsView} formattato per la presentazione nell'interfaccia utente (UI),
 * comprendente i dettagli del film, orari, disponibilità della sala e costi associati.
 * </p>
 */
public class GetProjectionResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Dettagli di visualizzazione della proiezione cinematografica.
	 */
	private ProjectionDetailsView projection;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetProjectionResponse() {
	}

	/**
	 * Costruisce una risposta specificando la vista dettagliata della proiezione.
	 *
	 * @param projection istanza di {@link ProjectionDetailsView} contenente i dati di visualizzazione
	 */
	public GetProjectionResponse(ProjectionDetailsView projection) {
		this.projection = projection;
	}

	/**
	 * Restituisce i dettagli di visualizzazione della proiezione.
	 *
	 * @return l'oggetto {@link ProjectionDetailsView}
	 */
	public ProjectionDetailsView getProjection() {
		return projection;
	}

	/**
	 * Imposta i dettagli di visualizzazione della proiezione da restituire al client.
	 *
	 * @param projection
	 */
	public void setProjection(ProjectionDetailsView projection) {
		this.projection = projection;
	} 
}