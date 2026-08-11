package cinemax.contracts.queries;
import java.math.BigDecimal;
import java.time.LocalDate;

import cinemax.contracts.interfaces.*;

public class GetProjectionsByFilmIdAndDate implements Query, ProjectionRequest  {

	private int film;	
	private LocalDate maxDataPrenotazione;
	
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
 