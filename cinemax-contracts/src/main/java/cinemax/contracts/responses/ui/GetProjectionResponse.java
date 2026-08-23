package cinemax.contracts.responses.ui;

import java.util.List;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.interfaces.Response;

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
