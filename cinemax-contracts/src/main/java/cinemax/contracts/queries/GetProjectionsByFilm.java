/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import java.time.LocalDate;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query parametrizzata per il recupero delle proiezioni relative a uno specifico film.
 * <p>
 * Incapsula l'identificativo del film e un limite temporale massimo entro cui
 * le proiezioni risultano prenotabili o visualizzabili dal client.
 * </p>
 */
public class GetProjectionsByFilm implements Query, ProjectionRequest {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco del film di cui richiedere le proiezioni.
	 */
	private int film;

	/**
	 * Data limite massima (inclusa) per la ricerca delle proiezioni prenotabili.
	 */
	private LocalDate maxDataPrenotazione;
	
	/**
	 * Costruisce un'istanza della query specificando l'identificatore del film e la data massima.
	 *
	 * @param film                 l'identificatore univoco del film richiesto
	 * @param maxDataPrenotazione  la data massima entro la quale cercare le proiezioni
	 */
	public GetProjectionsByFilm(int film, LocalDate maxDataPrenotazione) {
		super();
		this.film = film;
		this.maxDataPrenotazione = maxDataPrenotazione;
	}

	/**
	 * Restituisce l'identificatore del film associato alla ricerca.
	 *
	 * @return l'identificatore numerico del film
	 */
	public int getFilm() {
		return film;
	}

	/**
	 * Imposta l'identificatore del film per la ricerca delle proiezioni.
	 *
	 * @param film
	 */
	public void setFilm(int film) {
		this.film = film;
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
	 * @param maxDataPrenotazione
	 */
	public void setMaxDataPrenotazione(LocalDate maxDataPrenotazione) {
		this.maxDataPrenotazione = maxDataPrenotazione;
	}
}


