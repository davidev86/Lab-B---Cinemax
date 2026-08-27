/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.ProjectionRequest;

/**
 * Comando per la cancellazione di una proiezione esistente nel sistema.
 * <p>
 * Incapsula la richiesta di eliminazione tramite l'id 
 * della proiezione da rimuovere.
  */
public class DeleteProjection implements Command, ProjectionRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore della proiezione da cancellare.
	 */
	private Integer id;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public DeleteProjection() {
	}

	/**
	 * Crea un'istanza del comando specificando l'identificatore della proiezione.
	 *
	 * @param id 
	 */
	public DeleteProjection(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce l'identificatore della proiezione target.
	 * @return l'id, oppure null se non disponibile
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore della proiezione target.
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}