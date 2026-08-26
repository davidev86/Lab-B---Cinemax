/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;
import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per ottenere una specifica proiezione tramite il suo identificatore.
 */
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
 

