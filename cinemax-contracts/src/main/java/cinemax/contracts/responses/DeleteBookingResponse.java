/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server a seguito di una richiesta di cancellazione di una prenotazione.
 * Indica se l'operazione è andata a buon fine.
 */
public class DeleteBookingResponse implements Response {

	private static final long serialVersionUID = 1L;

	private boolean success;

	public DeleteBookingResponse() {
	}

	public DeleteBookingResponse(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}

