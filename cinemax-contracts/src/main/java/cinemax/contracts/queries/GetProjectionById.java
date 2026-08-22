package cinemax.contracts.queries;
import java.time.LocalDate;

import cinemax.contracts.interfaces.*;

public class GetProjectionById implements Query, ProjectionRequest  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idProiezione;	
	
	/**
	 * @param film
	 * @param maxDataPrenotazione
	 */
	public GetProjectionById(Integer idProiezione) {
		super();
		this.setIdProiezione(idProiezione);
	}

	public Integer getIdProiezione() {
		return idProiezione;
	}

	public void setIdProiezione(Integer idProiezione) {
		this.idProiezione = idProiezione;
	}

		
}
 