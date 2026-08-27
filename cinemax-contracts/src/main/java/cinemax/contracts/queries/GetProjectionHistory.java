/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per il recupero dello storico completo delle proiezioni cinematografiche passate.
 * <p>
 * Inviata dal client al server per ottenere l'elenco delle proiezioni la cui data
 * e ora di svolgimento risultano precedenti rispetto al momento della richiesta.
 * </p>
 */
public class GetProjectionHistory implements Query, ProjectionRequest {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore predefinito senza argomenti.
	 * <p>
	 * Inizializza la richiesta senza parametri specifici di filtro ed è necessario
	 * per i meccanismi di serializzazione e deserializzazione via socket TCP.
	 * </p>
	 */
	public GetProjectionHistory() {
		super();
	}	
}
 


