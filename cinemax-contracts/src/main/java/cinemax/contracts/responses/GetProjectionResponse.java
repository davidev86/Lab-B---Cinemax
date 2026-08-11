package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.interfaces.Response;

public class GetProjectionResponse implements Response {

	public GetProjectionResponse(List<ProjectionDetails> projections) {
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
