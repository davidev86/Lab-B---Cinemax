/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses.ui;

import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta contenente i dettagli completi di una singola proiezione cinematografica richiesta dal client.
 */
public class GetProjectionResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetProjectionResponse(ProjectionDetailsView projection) {
		this.projection = projection;
	}
	
	private ProjectionDetailsView projection;

	public ProjectionDetailsView getProjection() {
		return projection;
	}

	public void setProjection(ProjectionDetailsView projection) {
		this.projection = projection;
	} 
}


