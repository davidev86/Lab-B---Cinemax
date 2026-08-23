package cinemax.contracts.queries;
import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

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
 