package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.interfaces.Response;

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
