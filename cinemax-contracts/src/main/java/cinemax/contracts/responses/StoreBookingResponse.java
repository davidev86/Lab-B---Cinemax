package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

public class StoreBookingResponse implements Response {

	private static final long serialVersionUID = 1L;

	private Integer id;

	public StoreBookingResponse() {
	}

	public StoreBookingResponse(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}