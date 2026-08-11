package cinemax.serverCM.services.Utils;

import java.util.ArrayList;
import java.util.List;

public class SqlInsertBuilder {
    private final String tableName;
    private final List<String> columns = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();

    public SqlInsertBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Aggiunge una colonna e il relativo valore solo se il valore non è nullo.
     */
    public SqlInsertBuilder set(String column, Object value) {
        if (value != null) {
            if (value instanceof String && ((String) value).trim().isEmpty()) {
                return this;
            }
            columns.add(column);
            params.add(value);
        }
        return this;
    }

    public String getSql() {
        if (columns.isEmpty()) {
            throw new IllegalStateException("Impossibile costruire una INSERT senza colonne.");
        }

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" (");
        
        sql.append(String.join(", ", columns));
        
        sql.append(") VALUES (");
        
        // Genera i punti interrogativi per i PreparedStatements (?, ?, ?)
        List<String> placeholders = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            placeholders.add("?");
        }
        sql.append(String.join(", ", placeholders));
        
        sql.append(")");

        return sql.toString();
    }

    public List<Object> getParams() {
        return params;
    }
}