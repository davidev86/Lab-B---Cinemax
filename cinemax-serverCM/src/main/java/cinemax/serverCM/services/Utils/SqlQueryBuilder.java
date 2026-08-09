package cinemax.serverCM.services.Utils;

import java.util.ArrayList;
import java.util.List;

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
        return true;
    }

    public String getSql() {
        if (conditions.isEmpty()) {
            return baseSql;
        }
        
        // Se la baseSql contiene già un WHERE, le nuove condizioni vanno in AND
        String prefix = baseSql.toUpperCase().contains("WHERE") ? " AND " : " WHERE ";
        
        // Unisce tutte le condizioni aggiungendo " AND " solo tra di esse
        return baseSql + prefix + String.join(" AND ", conditions);
    }

    public List<Object> getParams() {
        return params;
    }
}