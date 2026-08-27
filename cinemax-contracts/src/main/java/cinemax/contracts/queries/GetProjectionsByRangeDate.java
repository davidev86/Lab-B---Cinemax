/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import java.time.LocalDateTime;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query parametrizzata per il recupero delle proiezioni cinematografiche comprese in un intervallo temporale (data e ora).
 * <p>
 * Incapsula i limiti temporali di inizio e fine intervallo inviati dal client al server
 * per estrarre tutte le proiezioni pianificate in tale periodo.
 * </p>
 */
public class GetProjectionsByRangeDate implements Query, ProjectionRequest {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Data e ora iniziali (incluse) dell'intervallo temporale di ricerca.
	 */
	private LocalDateTime daDataProiezione;

	/**
	 * Data e ora finali (incluse) dell'intervallo temporale di ricerca.
	 */
	private LocalDateTime aDataProiezione;
		
	/**
	 * Costruttore predefinito senza argomenti.

	 */
	public GetProjectionsByRangeDate() {
	}
	
	/**
	 * Costruisce un'istanza della query specificando l'intervallo temporale di ricerca.
	 *
	 * @param daDataProiezione la data e l'ora di inizio intervallo (può essere {@code null})
	 * @param aDataProiezione  la data e l'ora di fine intervallo (può essere {@code null})
	 */
	public GetProjectionsByRangeDate(LocalDateTime daDataProiezione, LocalDateTime aDataProiezione) {
		super();
		this.daDataProiezione = daDataProiezione;
		this.aDataProiezione = aDataProiezione;
	}

	/**
	 * Restituisce la data e l'ora finali dell'intervallo di ricerca.
	 *
	 * @return la data e ora di fine intervallo come {@link LocalDateTime}, oppure {@code null} se non impostata
	 */
	public LocalDateTime getaDataProiezione() {
		return aDataProiezione;
	}

	/**
	 * Imposta la data e l'ora finali dell'intervallo di ricerca.
	 *
	 * @param aDataProiezione la nuova data e ora di fine intervallo
	 */
	public void setaDataProiezione(LocalDateTime aDataProiezione) {
		this.aDataProiezione = aDataProiezione;
	}

	/**
	 * Restituisce la data e l'ora iniziali dell'intervallo di ricerca.
	 *
	 * @return la data e ora di inizio intervallo come {@link LocalDateTime}, oppure {@code null} se non impostata
	 */
	public LocalDateTime getDaDataProiezione() {
		return daDataProiezione;
	}

	/**
	 * Imposta la data e l'ora iniziali dell'intervallo di ricerca.
	 *
	 * @param daDataProiezione la nuova data e ora di inizio intervallo
	 */
	public void setDaDataProiezione(LocalDateTime daDataProiezione) {
		this.daDataProiezione = daDataProiezione;
	}	
}