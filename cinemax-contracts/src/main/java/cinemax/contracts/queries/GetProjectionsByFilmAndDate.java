/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import java.time.LocalDate;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query parametrizzata per il recupero delle proiezioni di un film ricercato per titolo.
 * <p>
 * Incapsula il titolo del film e un limite temporale massimo entro cui
 * filtrare le proiezioni disponibili per la consultazione o prenotazione.
 * </p>
 */
public class GetProjectionsByFilmAndDate implements Query, ProjectionRequest {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Titolo o parte del titolo del film di cui richiedere le proiezioni.
	 */
	private String titoloFilm;	

	/**
	 * Data limite massima (inclusa) per la ricerca delle proiezioni.
	 */
	private LocalDate maxDataPrenotazione;
	
	/**
	 * Costruisce un'istanza della query specificando il titolo del film e la data limite massima.
	 *
	 * @param film                il titolo del film da ricercare
	 * @param maxDataPrenotazione la data massima entro la quale cercare le proiezioni
	 */
	public GetProjectionsByFilmAndDate(String film, LocalDate maxDataPrenotazione) {
		super();
		this.titoloFilm = film;
		this.maxDataPrenotazione = maxDataPrenotazione;
	}

	/**
	 * Restituisce il titolo del film impostato come criterio di ricerca.
	 *
	 * @return il titolo del film
	 */
	public String getTitoloFilm() {
		return titoloFilm;
	}

	/**
	 * Imposta il titolo del film per la ricerca delle proiezioni.
	 *
	 * @param film il nuovo titolo del film da ricercare
	 */
	public void setTitoloFilm(String film) {
		this.titoloFilm = film;
	}

	/**
	 * Restituisce la data limite massima impostata per la ricerca delle proiezioni.
	 *
	 * @return la data massima di prenotazione come {@link LocalDate}
	 */
	public LocalDate getMaxDataPrenotazione() {
		return maxDataPrenotazione;
	}

	/**
	 * Imposta la data limite massima per la ricerca delle proiezioni.
	 *
	 * @param maxDataPrenotazione la nuova data massima di prenotazione
	 */
	public void setMaxDataPrenotazione(LocalDate maxDataPrenotazione) {
		this.maxDataPrenotazione = maxDataPrenotazione;
	}	
}