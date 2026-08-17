package cinemax.contracts.queries;

import cinemax.contracts.interfaces.Query;

public class GetBookingsByUserId implements Query {

	private static final long serialVersionUID = 1L;

	private Integer idUtente;

	public GetBookingsByUserId() {
	}

	public GetBookingsByUserId(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}
}