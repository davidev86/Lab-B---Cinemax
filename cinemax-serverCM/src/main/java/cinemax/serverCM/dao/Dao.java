/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.serverCM.dao;

import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;

/**
 * Interfaccia base per i DAO del serverCM.
 * Fornisce metodi per eseguire query e comandi, restituendo oggetti {@link Response}.
 */
public interface Dao {

	//il tipo di ritorno deve essere
	Response find(Query req);

	Response execute(Command req);

}

