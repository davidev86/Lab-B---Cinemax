package cinemax.serverCM.dao.utils;

import java.util.ArrayList;
import java.util.List;

public class SqlDeleteBuilder {

    private final String tableName;
    private final List<String> conditions = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();

    public SqlDeleteBuilder(String tableName) {
        this.tableName = tableName.trim();
    }

    public SqlDeleteBuilder where(String condition, Object value) {
        if (isValid(value)) {
            conditions.add(condition);
            params.add(value);
        }
        return this;
    }

    public SqlDeleteBuilder and(String condition, Object value) {
        return where(condition, value);
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
        String sql = "DELETE FROM " + tableName;
        if (!conditions.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", conditions);
        }
        return sql;
    }

    public List<Object> getParams() {
        return params;
    }
}