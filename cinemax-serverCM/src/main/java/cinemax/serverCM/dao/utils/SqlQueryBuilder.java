/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.serverCM.dao.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder per la costruzione dinamica di query SQL con supporto a condizioni WHERE aggregate in AND e gestione parametrizzata di valori.
 */
public class SqlQueryBuilder {
    private final String baseSql;
    private final List<String> conditions = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();

    public SqlQueryBuilder(String baseSql) {
        // Rimuove eventuali spazi e il punto e virgola finale per evitare errori di sintassi SQL
        this.baseSql = baseSql.trim();
    }

    public SqlQueryBuilder and(String condition, Object value) {
        if (isValid(value)) {
            conditions.add(condition);
            params.add(value);
        }
        return this;
    }

    private boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String && ((String) value).trim().isEmpty()) {
            return false;
        }
        
        if(value instanceof Integer && ((Integer) value) == 0)
        	return false;
        
        if (value instanceof BigInteger && ((BigInteger) value).compareTo(BigInteger.ZERO) == 0) {
            return false;
        }

        if (value instanceof BigDecimal && ((BigDecimal) value).compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }
        
        return true;
    }

    public String getSql() {
        if (conditions.isEmpty()) {
            return baseSql;
        }
        
        // Se la baseSql contiene giÃ  un WHERE, le nuove condizioni vanno in AND
        String prefix = baseSql.toUpperCase().contains("WHERE") ? " AND " : " WHERE ";
        
        // Unisce tutte le condizioni aggiungendo " AND " solo tra di esse
        return baseSql + prefix + String.join(" AND ", conditions);
    }

    public List<Object> getParams() {
        return params;
    }
}


