/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per il recupero di tutte le prenotazioni associate a un determinato utente.
 * <p>
 * Incapsula l'identificatore univoco dell'utente per consentire al server di estrarre
 * lo storico completo delle sue prenotazioni.
 * </p>
 */
public class GetBookingsByUserId implements Query, BookingRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco dell'utente di cui ricercare le prenotazioni.
	 */
	private Integer idUtente;

	/**
	 * Costruttore predefinito senza argomenti.
	 
	 */
	public GetBookingsByUserId() {
	}

	/**
	 * Crea un'istanza della query specificando l'identificatore dell'utente.
	 *
	 * @param idUtente
	 */
	public GetBookingsByUserId(Integer idUtente) {
		this.idUtente = idUtente;
	}

	/**
	 * Restituisce l'identificatore dell'utente associato alla ricerca.
	 *
	 * @return l'identificatore numerico dell'utente, oppure {@code null} se non specificato
	 */
	public Integer getIdUtente() {
		return idUtente;
	}

	/**
	 * Imposta l'identificatore dell'utente di cui ricercare le prenotazioni.
	 *
	 * @param idUtente
	 */
	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}
}

