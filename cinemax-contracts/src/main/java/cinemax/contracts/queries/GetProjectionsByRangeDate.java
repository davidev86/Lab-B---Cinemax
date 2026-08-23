package cinemax.contracts.queries;
import java.time.LocalDateTime;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

public class GetProjectionsByRangeDate implements Query, ProjectionRequest  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalDateTime daDataProiezione;
	private LocalDateTime aDataProiezione;
		
	public GetProjectionsByRangeDate() {
		
	}
	
	
	
	/**
	 * @param daDataProiezione
	 * @param aDataProiezione
	 */
	public GetProjectionsByRangeDate(LocalDateTime daDataProiezione, LocalDateTime aDataProiezione) {
		super();
		this.daDataProiezione = daDataProiezione;
		this.aDataProiezione = aDataProiezione;
	}
	

	public LocalDateTime getaDataProiezione() {
		return aDataProiezione;
	}
	public void setaDataProiezione(LocalDateTime aDataProiezione) {
		this.aDataProiezione = aDataProiezione;
	}
	public LocalDateTime getDaDataProiezione() {
		return daDataProiezione;
	}
	public void setDaDataProiezione(LocalDateTime daDataProiezione) {
		this.daDataProiezione = daDataProiezione;
	}	
}
 