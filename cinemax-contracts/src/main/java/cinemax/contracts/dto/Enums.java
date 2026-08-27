/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;
/**
 * Contenitore di enumerazioni utilizzate nei contratti e nei DTO per rappresentare valori
 * costanti scambiati tra client e server tramite protocollo TCP.
 */
public class Enums {

    /**
     * Enumerazione dei ruoli utente supportati dal sistema.
     * <p>
     * Mappa i privilegi autorizzativi dell'utente in conformità al vincolo
     * {@code CHECK} definito sulla tabella del database relazionale.
     * </p>
     */
    public enum Ruolo {

        /** Ruolo standard per gli utenti finali (clienti) del cinema. */
        CLIENTE("cliente"),

        /** Ruolo con privilegi di gestione e pianificazione delle proiezioni. */
        PROIEZIONISTA("proiezionista"),

        /** Ruolo con privilegi per l'emissione e la gestione dei biglietti. */
        BIGLIETTAIO("bigliettaio");

        /**
         * Stringa testuale associata al ruolo così come memorizzata nel database.
         */
        private final String dbValue;

        /**
         * Costruttore privato dell'enumerazione.
         *
         * @param dbValue il valore stringa persistito nel database
         */
        Ruolo(String dbValue) {
            this.dbValue = dbValue;
        }

        /**
         * Restituisce il valore del ruolo in formato compatibile con il database.
         *
         * @return la stringa identificativa del ruolo nel database
         */
        public String getDbValue() {
            return dbValue;
        }

        /**
         * Converte una stringa proveniente dal database nella corrispondente costante {@link Ruolo}.
         * <p>
         * La comparazione è case-insensitive (non fa distinzione tra maiuscole e minuscole).
         * </p>
         *
         * @param value la stringa estratta dal database da convertire
         * @return l'istanza di {@link Ruolo} corrispondente, oppure {@code null} se il parametro di input è {@code null}
         * @throws IllegalArgumentException se la stringa fornita non corrisponde a nessun ruolo valido
         */
        public static Ruolo fromDbValue(String value) {
            if (value == null) return null;
            for (Ruolo r : Ruolo.values()) {
                if (r.dbValue.equalsIgnoreCase(value)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("Ruolo non valido: " + value);
        }
    }
}