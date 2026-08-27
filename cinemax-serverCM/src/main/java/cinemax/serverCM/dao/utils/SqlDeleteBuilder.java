/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
 */
package cinemax.serverCM.dao.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Costruttore fluente (builder) per la composizione dinamica di istruzioni SQL di tipo {@code DELETE}.
 * <p>
 * Consente di definire la tabella target e concatenare clausole condizionali {@code WHERE} con operatore logico {@code AND},
 * gestendo automaticamente l'esclusione di parametri nulli o stringhe vuote e mantenendo sincronizzata
 * la lista dei valori posizionali associati.
 * </p>
 */
public class SqlDeleteBuilder {

    private final String tableName;
    private final List<String> conditions = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();

    /**
     * Inizializza il builder specificando il nome della tabella da cui eliminare i record.
     *
     * @param tableName il nome della tabella target (es. {@code public."Prenotazioni"})
     */
    public SqlDeleteBuilder(String tableName) {
        this.tableName = tableName.trim();
    }

    /**
     * Aggiunge una condizione alla clausola {@code WHERE} se il valore fornito è valido (non nullo e non vuoto).
     *
     * @param condition l'espressione condizionale SQL con segnaposto (es. {@code "id = ?"})
     * @param value     il valore da associare al segnaposto
     * @return l'istanza corrente del builder per consentire il method chaining
     */
    public SqlDeleteBuilder where(String condition, Object value) {
        if (isValid(value)) {
            conditions.add(condition);
            params.add(value);
        }
        return this;
    }

    /**
     * Aggiunge un'ulteriore condizione in {@code AND} alla clausola di cancellazione.
     *
     * @param condition l'espressione condizionale SQL con segnaposto
     * @param value     il valore da associare al parametro
     * @return l'istanza corrente del builder
     */
    public SqlDeleteBuilder and(String condition, Object value) {
        return where(condition, value);
    }

    /**
     * Verifica se il parametro specificato è valido per l'inclusione nella query.
     *
     * @param value l'oggetto parametro da validare
     * @return {@code true} se il valore non è nullo e non corrisponde a una stringa vuota/blank, {@code false} altrimenti
     */
    private boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String && ((String) value).trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Compone e restituisce la stringa SQL finale dell'istruzione {@code DELETE}.
     *
     * @return l'istruzione SQL formattata completa di clausole {@code WHERE} (se presenti)
     */
    public String getSql() {
        String sql = "DELETE FROM " + tableName;
        if (!conditions.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", conditions);
        }
        return sql;
    }

    /**
     * Restituisce la lista ordinata dei parametri associati ai segnaposto posizionali definiti nella query.
     *
     * @return la lista dei valori dei parametri da passare al {@link java.sql.PreparedStatement}
     */
    public List<Object> getParams() {
        return params;
    }
}