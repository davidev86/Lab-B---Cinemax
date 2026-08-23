package cinemax.contracts.queries;
import java.time.LocalDate;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

public class GetProjectionsByFilm implements Query, ProjectionRequest  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int film;	
	private LocalDate maxDataPrenotazione;
	
	/**
	 * @param film
	 * @param maxDataPrenotazione
	 */
	public GetProjectionsByFilm(int film, LocalDate maxDataPrenotazione) {
		super();
		this.film = film;
		this.maxDataPrenotazione = maxDataPrenotazione;
	}
	public int getFilm() {
		return film;
	}
	public void setFilm(int film) {
		this.film = film;
	}
	public LocalDate getMaxDataPrenotazione() {
		return maxDataPrenotazione;
	}
	public void setMaxDataPrenotazione(LocalDate maxDataPrenotazione) {
		this.maxDataPrenotazione = maxDataPrenotazione;
	}

	
	
}
 