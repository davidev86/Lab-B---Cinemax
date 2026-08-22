package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.interfaces.Response;

public class GetFilmsResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetFilmsResponse(List<FilmDetails> projections) {
		this.projections = projections;
	}
	
	private List<FilmDetails> projections;

	public List<FilmDetails> getProjections() {
		return projections;
	}

	public void setProjections(List<FilmDetails> projections) {
		this.projections = projections;
	}
}
