package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.interfaces.Response;

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