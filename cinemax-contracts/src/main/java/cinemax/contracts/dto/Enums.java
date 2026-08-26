/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

/**
 * Contenitore di enumerazioni utilizzate nei DTO per rappresentare valori
 * costanti e finiti (ruoli utente) scambiati tra client e server via TCP.
 */
public class Enums {
	// Enum per gestire il vincolo CHECK sul ruolo ('cliente', 'proiezionista', 'bigliettaio')
    /**
     * Enumerazione dei ruoli utente nel sistema (CLIENTE, PROIEZIONISTA, BIGLIETTAIO).
    /**
     * Ogni ruolo è associato a un valore stringa corrispondente nel database.
     */
    public enum Ruolo {
        CLIENTE("cliente"),
        PROIEZIONISTA("proiezionista"),
        BIGLIETTAIO("bigliettaio");

        private final String dbValue;

        Ruolo(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return dbValue;
        }

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


