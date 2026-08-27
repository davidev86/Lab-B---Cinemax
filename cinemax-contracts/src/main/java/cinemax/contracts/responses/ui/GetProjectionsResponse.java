/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses.ui;

import java.util.List;

import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server contenente l'elenco delle viste dettagliate delle proiezioni cinematografiche.
 * <p>
 * Incapsula una collezione di oggetti {@link ProjectionDetailsView} pronti per la consultazione
 * e la presentazione grafica all'interno dei componenti dell'interfaccia utente (UI).
 * </p>
 */
public class GetProjectionsResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Elenco contenente le viste dettagliate delle proiezioni cinematografiche.
	 */
	private List<ProjectionDetailsView> projections;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetProjectionsResponse() {
	}

	/**
	 * Costruisce una risposta specificando la lista di viste dettagliate delle proiezioni.
	 *
	 * @param projections lista di istanze {@link ProjectionDetailsView} da incapsulare nella risposta
	 */
	public GetProjectionsResponse(List<ProjectionDetailsView> projections) {
		this.projections = projections;
	}

	/**
	 * Restituisce la lista delle viste dettagliate delle proiezioni.
	 *
	 * @return la lista di {@link ProjectionDetailsView}
	 */
	public List<ProjectionDetailsView> getProjections() {
		return projections;
	}

	/**
	 * Imposta la lista delle viste dettagliate delle proiezioni.
	 *
	 * @param projections
	 */
	public void setProjections(List<ProjectionDetailsView> projections) {
		this.projections = projections;
	}
}