package cinemax.contracts.commands;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Command;

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