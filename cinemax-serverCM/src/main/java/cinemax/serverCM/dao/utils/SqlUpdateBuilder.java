/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.serverCM.dao.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder per la costruzione di query SQL UPDATE con supporto dinamico per clausole SET e WHERE.
 */
public class SqlUpdateBuilder {
    private final String tableName;
    private final List<String> setColumns = new ArrayList<>();
    private final List<Object> setParams = new ArrayList<>();
    private final List<String> whereClauses = new ArrayList<>();
    private final List<Object> whereParams = new ArrayList<>();

    public SqlUpdateBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Aggiunge una colonna da aggiornare e il relativo valore solo se non nullo/vuoto.
     */
    public SqlUpdateBuilder set(String column, Object value) {
        if (value != null) {
            if (value instanceof String && ((String) value).trim().isEmpty()) {
                return this;
            }
            setColumns.add(column);
            setParams.add(value);
        }
        return this;
    }

    /**
     * Aggiunge una condizione WHERE con uguaglianza (es. "id", 5 -> "id = ?").
     */
    public SqlUpdateBuilder where(String column, Object value) {
        if (value != null) {
            whereClauses.add(column + " = ?");
            whereParams.add(value);
        }
        return this;
    }

    public String getSql() {
        if (setColumns.isEmpty()) {
            throw new IllegalStateException("Impossibile costruire una UPDATE senza campi da aggiornare.");
        }
        if (whereClauses.isEmpty()) {
            throw new IllegalStateException("Impossibile costruire una UPDATE senza clausola WHERE.");
        }

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");

        // Genera la lista "col1 = ?, col2 = ?"
        List<String> assignments = new ArrayList<>();
        for (String col : setColumns) {
            assignments.add(col + " = ?");
        }
        sql.append(String.join(", ", assignments));

        // Genera la parte "WHERE cond1 = ? AND cond2 = ?"
        sql.append(" WHERE ").append(String.join(" AND ", whereClauses));

        return sql.toString();
    }

    /**
     * Ritorna tutti i parametri unificati nell'ordine esatto richiesto dal PreparedStatement:
     * prima i parametri del SET, poi quelli della clausola WHERE.
     */
    public List<Object> getParams() {
        List<Object> allParams = new ArrayList<>(setParams);
        allParams.addAll(whereParams);
        return allParams;
    }
}


