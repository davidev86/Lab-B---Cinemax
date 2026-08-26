/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.serverCM.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
/**
 * Interfaccia funzionale generica per mappare righe di un ResultSet SQL a oggetti di tipo arbitrario, utilizzata nei DAO per la conversione dei dati.
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
}


