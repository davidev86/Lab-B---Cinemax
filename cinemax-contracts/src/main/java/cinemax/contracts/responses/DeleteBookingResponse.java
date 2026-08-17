package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

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