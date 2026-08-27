/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */

package cinemax.contracts.commands;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Command;

/**
 * Comando per la cancellazione di una prenotazione esistente.
 * <p>
 * Incapsula la richiesta di eliminazione specificando l'identificatore univoco 
 * della prenotazione da rimuovere dal sistema.
 * </p>
 */
public class DeleteBooking implements Command, BookingRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco della prenotazione da cancellare.
	 */
	private Integer id;

	/**
	 * Costruttore predefinito senza argomenti.
	 * <p>
	 */
	public DeleteBooking() {
	}

	/**
	 * Crea una nuova istanza del comando specificando l'identificatore della prenotazione.
	 *
	 * @param id
	 */
	public DeleteBooking(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce l'identificatore della prenotazione target.
	 *
	 * @return l'id della prenotazione
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore della prenotazione target.
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}