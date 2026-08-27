/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server al client a seguito di una richiesta di cancellazione di una proiezione.
 */
public class DeleteProjectionResponse implements Response {

	private static final long serialVersionUID = 1L;

	/**
	 * Boolean che indica se l'operazione di cancellazione della proiezione è andata a buon fine.
	 */
	private boolean success;

	/**
	 * Costruttore predefinito senza argomenti.
	 * <p>
	 * Necessario per i meccanismi di serializzazione, deserializzazione o binding via socket TCP.
	 * </p>
	 */
	public DeleteProjectionResponse() {
	}

	/**
	 * Costruisce una risposta specificando l'esito della cancellazione della proiezione.
	 *
	 * @param success
	 */
	public DeleteProjectionResponse(boolean success) {
		this.success = success;
	}

	/**
	 * Restituisce l'esito dell'operazione di cancellazione della proiezione.
	 *
	 * @return {@code true} se la proiezione è stata cancellata con successo, {@code false} altrimenti
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Imposta l'esito dell'operazione di cancellazione della proiezione.
	 *
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}