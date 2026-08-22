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

	public GetProjectionResponse(List<ProjectionDetailsView> projections) {
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
