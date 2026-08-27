/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import java.time.LocalDate;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per la ricerca e il filtraggio delle prenotazioni nel sistema.
 * <p>
 * Incapsula i criteri di ricerca (codice identificativo, anagrafica cliente, 
 * titolo del film e intervallo temporale delle proiezioni) inviati dal client al server.
 * I campi non valorizzati (pari a {@code null}) non vengono applicati come filtri.
 * </p>
 */
public class GetBookings implements Query, BookingRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Codice univoco identificativo della prenotazione da ricercare.
	 */
	private Integer codicePrenotazione;

	/**
	 * Nome del cliente associato alla prenotazione (filtro parziale o esatto).
	 */
	private String nomeCliente;

	/**
	 * Cognome del cliente associato alla prenotazione (filtro parziale o esatto).
	 */
	private String cognomeCliente;

	/**
	 * Titolo del film associato alla proiezione prenotata (filtro parziale o esatto).
	 */
	private String titoloFilm;

	/**
	 * Data iniziale (inclusa) dell'intervallo temporale di ricerca proiezioni.
	 */
	private LocalDate daDataProiezione;

	/**
	 * Data finale (inclusa) dell'intervallo temporale di ricerca proiezioni.
	 */
	private LocalDate aDataProiezione;

	/**
	 * Costruttore predefinito senza argomenti.

	 */
	public GetBookings() {
	}

	/**
	 * Costruttore parametrizzato per inizializzare tutti i criteri di filtro della query.
	 *
	 * @param codicePrenotazione il codice univoco della prenotazione (può essere {@code null})
	 * @param nomeCliente        il nome del cliente (può essere {@code null})
	 * @param cognomeCliente     il cognome del cliente (può essere {@code null})
	 * @param titoloFilm         il titolo del film (può essere {@code null})
	 * @param daDataProiezione   la data di inizio intervallo (può essere {@code null})
	 * @param aDataProiezione    la data di fine intervallo (può essere {@code null})
	 */
	public GetBookings(Integer codicePrenotazione, String nomeCliente, String cognomeCliente, 
	                   String titoloFilm, LocalDate daDataProiezione, LocalDate aDataProiezione) {
		this.codicePrenotazione = codicePrenotazione;
		this.nomeCliente = nomeCliente;
		this.cognomeCliente = cognomeCliente;
		this.titoloFilm = titoloFilm;
		this.daDataProiezione = daDataProiezione;
		this.aDataProiezione = aDataProiezione;
	}

	/**
	 * Restituisce il codice della prenotazione impostato come filtro.
	 *
	 * @return l'identificativo della prenotazione
	 */
	public Integer getCodicePrenotazione() {
		return codicePrenotazione;
	}

	/**
	 * Imposta il codice della prenotazione come filtro di ricerca.
	 *
	 * @param codicePrenotazione
	 */
	public void setCodicePrenotazione(Integer codicePrenotazione) {
		this.codicePrenotazione = codicePrenotazione;
	}

	/**
	 * Restituisce il nome del cliente impostato come filtro.
	 *
	 * @return il nome del cliente, oppure null se non disponibile
	 */
	public String getNomeCliente() {
		return nomeCliente;
	}

	/**
	 * Imposta il nome del cliente come criterio di ricerca.
	 *
	 * @param nomeCliente
	 */
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	/**
	 * Restituisce il cognome del cliente impostato come filtro.
	 *
	 * @return il cognome del cliente, oppure null se non disponibile
	 */
	public String getCognomeCliente() {
		return cognomeCliente;
	}

	/**
	 * Imposta il cognome del cliente come criterio di ricerca.
	 *
	 * @param cognomeCliente
	 */
	public void setCognomeCliente(String cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}

	/**
	 * Restituisce il titolo del film impostato come filtro.
	 *
	 * @return il titolo del film, oppure null se non disponibile
	 */
	public String getTitoloFilm() {
		return titoloFilm;
	}

	/**
	 * Imposta il titolo del film come criterio di ricerca.
	 *
	 * @param titoloFilm
	 */
	public void setTitoloFilm(String titoloFilm) {
		this.titoloFilm = titoloFilm;
	}

	/**
	 * Restituisce la data iniziale dell'intervallo di ricerca.
	 *
	 * @return la data di inizio intervallo come {@link LocalDate}, oppure null se non disponibile
	 */
	public LocalDate getDaDataProiezione() {
		return daDataProiezione;
	}

	/**
	 * Imposta la data iniziale dell'intervallo di ricerca proiezioni.
	 *
	 * @param daDataProiezione
	 */
	public void setDaDataProiezione(LocalDate daDataProiezione) {
		this.daDataProiezione = daDataProiezione;
	}

	/**
	 * Restituisce la data finale dell'intervallo di ricerca.
	 *
	 * @return la data di fine intervallo come {@link LocalDate}, oppure null se non disponibile
	 */
	public LocalDate getADataProiezione() {
		return aDataProiezione;
	}

	/**
	 * Imposta la data finale dell'intervallo di ricerca proiezioni.
	 *
	 * @param aDataProiezione
	 */
	public void setADataProiezione(LocalDate aDataProiezione) {
		this.aDataProiezione = aDataProiezione;
	}
}