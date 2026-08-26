/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.ProjectionRequest;

/**
 * Command per la cancellazione di una proiezione identificata dal suo id.
 */
public class DeleteProjection implements Command, ProjectionRequest {

	private static final long serialVersionUID = 1L;

	private Integer id;

	public DeleteProjection() {
	}

	public DeleteProjection(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

