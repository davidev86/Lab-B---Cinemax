/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
 */
package cinemax.serverCM.dao.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Costruttore fluente (builder) per la composizione dinamica e parametrizzata di query SQL {@code INSERT}.
 * <p>
 * Consente di specificare le colonne e i rispettivi valori da inserire all'interno della tabella target,
 * generando in modo automatico i segnaposto posizionali ({@code ?}) per l'uso con {@link java.sql.PreparedStatement}
 * ed escludendo attributi nulli o stringhe vuote.
 * </p>
 */
public class SqlInsertBuilder {

    private final String tableName;
    private final List<String> columns = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();

    /**
     * Inizializza il builder specificando il nome della tabella in cui eseguire l'inserimento.
     *
     * @param tableName il nome della tabella target (es. {@code public."Utenti"})
     */
    public SqlInsertBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Aggiunge una colonna e il corrispondente valore all'istruzione di inserimento solo se il valore
     * risulta valido (non nullo e non corrispondente a una stringa vuota).
     *
     * @param column il nome della colonna nel database
     * @param value  il valore dell'attributo da inserire
     * @return l'istanza corrente del builder per supportare la concatenazione (method chaining)
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

    /**
     * Compone e restituisce la stringa SQL finale dell'istruzione {@code INSERT INTO}.
     *
     * @return l'istruzione SQL formattata con la lista delle colonne e i relativi segnaposto {@code ?}
     * @throws IllegalStateException se non è stata impostata alcuna colonna valida per l'inserimento
     */
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

    /**
     * Restituisce l'elenco ordinato dei parametri corrispondenti ai segnaposto definiti nella clausola {@code VALUES}.
     *
     * @return la lista dei valori dei parametri da passare al {@link java.sql.PreparedStatement}
     */
    public List<Object> getParams() {
        return params;
    }
}