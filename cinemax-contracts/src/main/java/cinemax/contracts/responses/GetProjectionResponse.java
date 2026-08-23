package cinemax.contracts.responses;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.interfaces.Response;

public class GetProjectionResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetProjectionResponse(ProjectionDetails projection) {
		this.projection = projection;
	}
	
	private ProjectionDetails projection;

	public ProjectionDetails getProjection() {
		return projection;
	}

	public void setProjections(ProjectionDetails projection) {
		this.projection = projection;
	}
}
