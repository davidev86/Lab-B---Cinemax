/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per recuperare tutte le prenotazioni associate a un utente specifico dal server.
 */
public class GetBookingsByUserId implements Query,BookingRequest {

	private static final long serialVersionUID = 1L;

	private Integer idUtente;

	public GetBookingsByUserId() {
	}

	public GetBookingsByUserId(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}
}


