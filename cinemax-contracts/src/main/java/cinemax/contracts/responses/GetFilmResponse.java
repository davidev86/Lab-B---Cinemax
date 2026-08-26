/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta che incapsula i dettagli completi di un singolo film.
 */
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


