/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import java.time.LocalDate;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per recuperare le prenotazioni associate a una data specifica.
 */
public class GetBookingsByDate implements Query, BookingRequest {

	private static final long serialVersionUID = 1L;

	private LocalDate date;


	public GetBookingsByDate() {
	}

	public GetBookingsByDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


}


