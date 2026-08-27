/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server contenente l'elenco delle proiezioni cinematografiche trovate.
 * <p>
 * Incapsula una lista di oggetti {@link ProjectionDetails} restituita a seguito dell'esecuzione
 * di una query di ricerca o filtraggio proiezioni (per film, data o criteri avanzati).
 * </p>
 */
public class GetProjectionsResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Elenco contenente i dettagli delle proiezioni risultanti dalla query.
	 */
	private List<ProjectionDetails> projections;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetProjectionsResponse() {
	}

	/**
	 * Costruisce una risposta contenente la lista specificata di proiezioni.
	 *
	 * @param projections la lista di istanze {@link ProjectionDetails} da incapsulare nella risposta
	 */
	public GetProjectionsResponse(List<ProjectionDetails> projections) {
		this.projections = projections;
	}

	/**
	 * Restituisce la lista dei dettagli delle proiezioni.
	 *
	 * @return la lista di {@link ProjectionDetails}
	 */
	public List<ProjectionDetails> getProjections() {
		return projections;
	}

	/**
	 * Imposta la lista dei dettagli delle proiezioni.
	 *
	 * @param projections 
	 */
	public void setProjections(List<ProjectionDetails> projections) {
		this.projections = projections;
	}
}