package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.interfaces.Response;

public class GetFilmsResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetFilmsResponse(List<FilmDetails> films) {
		this.films = films;
	}
	
	private List<FilmDetails> films;

	public List<FilmDetails> getFilms() {
		return films;
	}

	public void setFilms(List<FilmDetails> films) {
		this.films = films;
	}
}
