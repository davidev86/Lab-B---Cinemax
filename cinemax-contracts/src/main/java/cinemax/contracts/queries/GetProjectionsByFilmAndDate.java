package cinemax.contracts.queries;
import java.time.LocalDate;

import cinemax.contracts.interfaces.*;

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
 