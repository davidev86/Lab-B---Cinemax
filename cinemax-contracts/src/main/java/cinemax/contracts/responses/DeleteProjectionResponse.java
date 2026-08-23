package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

public class DeleteProjectionResponse implements Response {

	private static final long serialVersionUID = 1L;

	private boolean success;

	public DeleteProjectionResponse() {
	}

	public DeleteProjectionResponse(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}