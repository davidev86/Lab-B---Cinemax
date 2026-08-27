/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
 */
package cinemax.serverCM.dao.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Costruttore fluente (builder) per la composizione dinamica e parametrizzata di query SQL {@code UPDATE}.
 * <p>
 * Permette di configurare le coppie colonna-valore per la clausola {@code SET} e le condizioni di uguaglianza
 * per la clausola {@code WHERE}, gestendo automaticamente l'esclusione di parametri nulli o stringhe vuote
 * e ordinando i parametri posizionali nell'ordine esatto richiesto da {@link java.sql.PreparedStatement}.
 * </p>
 */
public class SqlUpdateBuilder {

    private final String tableName;
    private final List<String> setColumns = new ArrayList<>();
    private final List<Object> setParams = new ArrayList<>();
    private final List<String> whereClauses = new ArrayList<>();
    private final List<Object> whereParams = new ArrayList<>();

    /**
     * Inizializza il builder specificando la tabella target dell'aggiornamento.
     *
     * @param tableName il nome della tabella del database (es. {@code public."Utenti"})
     */
    public SqlUpdateBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Aggiunge una colonna da aggiornare e il relativo valore alla clausola {@code SET}
     * solo se il valore non è nullo o non corrisponde a una stringa vuota.
     *
     * @param column il nome della colonna da modificare
     * @param value  il nuovo valore da assegnare alla colonna
     * @return l'istanza corrente del builder per supportare il method chaining
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
     * Aggiunge una condizione di uguaglianza alla clausola {@code WHERE} (es. {@code "id = ?"}).
     *
     * @param column il nome della colonna su cui applicare la condizione di filtro
     * @param value  il valore del vincolo di uguaglianza
     * @return l'istanza corrente del builder
     */
    public SqlUpdateBuilder where(String column, Object value) {
        if (value != null) {
            whereClauses.add(column + " = ?");
            whereParams.add(value);
        }
        return this;
    }

    /**
     * Compone e restituisce la stringa SQL finale dell'istruzione {@code UPDATE}.
     *
     * @return la query SQL formattata completa di assegnazioni {@code SET} e condizioni {@code WHERE}
     * @throws IllegalStateException se non è specificata alcuna colonna da modificare o manca la clausola {@code WHERE}
     */
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
     * Restituisce la lista completa e ordinata dei parametri associati ai segnaposto posizionali ({@code ?}):
     * include prima i parametri della clausola {@code SET} e successivamente quelli della clausola {@code WHERE}.
     *
     * @return la lista combinata dei valori dei parametri da passare al {@link java.sql.PreparedStatement}
     */
    public List<Object> getParams() {
        List<Object> allParams = new ArrayList<>(setParams);
        allParams.addAll(whereParams);
        return allParams;
    }
}