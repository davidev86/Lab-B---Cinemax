/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;
import java.time.LocalDate;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query parametrizzata per recuperare le proiezioni di un film identificato dal titolo fino a una data massima di prenotazione.
 */
public class GetProjectionsByFilmAndDate implements Query, ProjectionRequest  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String titoloFilm;	
	private LocalDate maxDataPrenotazione;
	
	/**
	 * @param film
	 * @param maxDataPrenotazione
	 */
	public GetProjectionsByFilmAndDate(String film, LocalDate maxDataPrenotazione) {
		super();
		this.titoloFilm = film;
		this.maxDataPrenotazione = maxDataPrenotazione;
	}
	public String getTitoloFilm() {
		return titoloFilm;
	}
	public void setTitoloFilm(String film) {
		this.titoloFilm = film;
	}
	public LocalDate getMaxDataPrenotazione() {
		return maxDataPrenotazione;
	}
	public void setMaxDataPrenotazione(LocalDate maxDataPrenotazione) {
		this.maxDataPrenotazione = maxDataPrenotazione;
	}	
}
 


