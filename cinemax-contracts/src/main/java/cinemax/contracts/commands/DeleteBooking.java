/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Command;

/**
 * Command per la cancellazione di una prenotazione identificata dal suo id.
 */
public class DeleteBooking implements Command,BookingRequest {

	private static final long serialVersionUID = 1L;

	private Integer id;

	public DeleteBooking() {
	}

	public DeleteBooking(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

