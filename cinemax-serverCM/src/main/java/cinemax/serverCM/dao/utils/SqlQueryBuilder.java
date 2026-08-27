/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
 */
package cinemax.serverCM.dao.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Costruttore fluente (builder) per la composizione dinamica di query SQL di selezione con clausole condizionali.
 * <p>
 * Permette di accodare dinamicamente condizioni in {@code AND} verificando preventivamente la validità
 * dei parametri associati (scartando valori nulli, stringhe vuote o valori numerici pari a zero) e
 * gestendo l'eventuale presenza pregressa della clausola {@code WHERE} all'interno della query base.
 * </p>
 */
public class SqlQueryBuilder {

    private final String baseSql;
    private final List<String> conditions = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();

    /**
     * Inizializza il builder impostando l'istruzione SQL di base.
     *
     * @param baseSql la stringa SQL iniziale (es. {@code "SELECT * FROM public.\"Proiezioni_pianificate\""})
     */
    public SqlQueryBuilder(String baseSql) {
        // Rimuove eventuali spazi e il punto e virgola finale per evitare errori di sintassi SQL
        this.baseSql = baseSql.trim();
    }

    /**
     * Aggiunge una condizione in {@code AND} alla query qualora il valore associato risulti valido.
     *
     * @param condition il frammento condizionale SQL con segnaposto (es. {@code "titolofilm ILIKE ?"})
     * @param value     il valore da associare al parametro posizionale
     * @return l'istanza corrente del builder per supportare il method chaining
     */
    public SqlQueryBuilder and(String condition, Object value) {
        if (isValid(value)) {
            conditions.add(condition);
            params.add(value);
        }
        return this;
    }

    /**
     * Verifica la validità del valore del parametro per stabilire se applicare o meno il filtro.
     * <p>
     * Vengono considerati non validi:
     * <ul>
     *   <li>Riferimenti {@code null};</li>
     *   <li>Stringhe vuote o composte solo da spazi bianchi;</li>
     *   <li>Valori numerici interi uguali a {@code 0};</li>
     *   <li>Istanze di {@link BigInteger} o {@link BigDecimal} uguali a zero.</li>
     * </ul>
     * </p>
     *
     * @param value l'oggetto parametro da validare
     * @return {@code true} se il valore è significativo ai fini del filtraggio, {@code false} altrimenti
     */
    private boolean isValid(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String && ((String) value).trim().isEmpty()) {
            return false;
        }
        
        if (value instanceof Integer && ((Integer) value) == 0) {
            return false;
        }
        
        if (value instanceof BigInteger && ((BigInteger) value).compareTo(BigInteger.ZERO) == 0) {
            return false;
        }

        if (value instanceof BigDecimal && ((BigDecimal) value).compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }
        
        return true;
    }

    /**
     * Compone e restituisce l'istruzione SQL completa di tutte le clausole condizionali applicate.
     *
     * @return la stringa SQL finale pronta per l'esecuzione
     */
    public String getSql() {
        if (conditions.isEmpty()) {
            return baseSql;
        }
        
        // Se la baseSql contiene già un WHERE, le nuove condizioni vanno in AND
        String prefix = baseSql.toUpperCase().contains("WHERE") ? " AND " : " WHERE ";
        
        // Unisce tutte le condizioni aggiungendo " AND " solo tra di esse
        return baseSql + prefix + String.join(" AND ", conditions);
    }

    /**
     * Restituisce l'elenco ordinato degli oggetti parametro corrispondenti ai segnaposto {@code ?} inclusi nella query.
     *
     * @return la lista dei parametri da associare al {@link java.sql.PreparedStatement}
     */
    public List<Object> getParams() {
        return params;
    }
}