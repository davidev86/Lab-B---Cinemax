/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses.ui;

import java.util.List;

import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta contenente la lista completa delle proiezioni cinematografiche disponibili con i loro dettagli.
 */
public class GetProjectionsResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetProjectionsResponse(List<ProjectionDetailsView> projections) {
		this.projections = projections;
	}
	
	private List<ProjectionDetailsView> projections;

	public List<ProjectionDetailsView> getProjections() {
		return projections;
	}

	public void setProjections(List<ProjectionDetailsView> projections) {
		this.projections = projections;
	}
}


