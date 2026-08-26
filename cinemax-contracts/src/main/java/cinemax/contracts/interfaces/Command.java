/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.interfaces;

import java.io.Serializable;

/**
 * Interfaccia radice che definisce il contratto base per tutti i comandi del sistema, estendendo Serializable per la trasmissione di rete.
 */
public interface Command extends Serializable {
	
	public Integer getId();
	
	
}


