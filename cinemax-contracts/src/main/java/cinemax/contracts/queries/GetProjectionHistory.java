/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;
import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per recuperare la cronologia completa di tutte le proiezioni cinematografiche passate.
 */
public class GetProjectionHistory implements Query, ProjectionRequest  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 */
	public GetProjectionHistory() {
		super();
	}	
}
 


