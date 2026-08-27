/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server al client a seguito di una richiesta di cancellazione di una prenotazione.
 * <p>
 * Incapsula l'esito booleano dell'operazione di eliminazione eseguita sul database.
 * </p>
 */
public class DeleteBookingResponse implements Response {

	private static final long serialVersionUID = 1L;

	/**
	 * Boolean che indica se l'operazione di cancellazione è andata a buon fine.
	 */
	private boolean success;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public DeleteBookingResponse() {
	}

	/**
	 * Costruisce una risposta specificando l'esito della cancellazione.
	 *
	 * @param success {@code true} se la cancellazione è stata completata con successo, {@code false} altrimenti
	 */
	public DeleteBookingResponse(boolean success) {
		this.success = success;
	}

	/**
	 * Restituisce l'esito dell'operazione di cancellazione.
	 *
	 * @return {@code true} se la prenotazione è stata cancellata con successo, {@code false} altrimenti
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Imposta l'esito dell'operazione di cancellazione.
	 *
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}