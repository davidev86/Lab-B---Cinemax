/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Oggetto di risposta che incapsula la lista dei dettagli delle prenotazioni restituite dal server in risposta a una query di ricerca.
 */
public class GetBookingResponse implements Response {

	private static final long serialVersionUID = 1L;

	private List<BookingDetails> bookings;

	public GetBookingResponse() {
	}

	public GetBookingResponse(List<BookingDetails> bookings) {
		this.bookings = bookings;
	}

	public List<BookingDetails> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingDetails> bookings) {
		this.bookings = bookings;
	}
}


