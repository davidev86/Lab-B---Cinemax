/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta che incapsula una collezione di dettagli di proiezioni cinematografiche.
 */
public class GetProjectionsResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetProjectionsResponse(List<ProjectionDetails> projections) {
		this.projections = projections;
	}
	
	private List<ProjectionDetails> projections;

	public List<ProjectionDetails> getProjections() {
		return projections;
	}

	public void setProjections(List<ProjectionDetails> projections) {
		this.projections = projections;
	}
}


