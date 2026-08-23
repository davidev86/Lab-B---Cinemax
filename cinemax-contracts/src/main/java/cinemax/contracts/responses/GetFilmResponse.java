package cinemax.contracts.responses;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.interfaces.Response;

public class GetFilmResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetFilmResponse(FilmDetails projection) {
		this.projection = projection;
	}
	
	private FilmDetails projection;

	public FilmDetails getFilm() {
		return projection;
	}

	public void setFilm(FilmDetails projection) {
		this.projection = projection;
	}
}
