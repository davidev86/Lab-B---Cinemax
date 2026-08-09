package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.Projection;
import cinemax.contracts.interfaces.Response;

public class GetProjectionResponse implements Response {

	public GetProjectionResponse(List<Projection> projections) {
		this.projections = projections;
	}
	
	private List<Projection> projections;

	public List<Projection> getProjections() {
		return projections;
	}

	public void setProjections(List<Projection> projections) {
		this.projections = projections;
	}
}
