package cinemax.contracts.dto;

public class Enums {
	// Enum per gestire il vincolo CHECK sul ruolo ('cliente', 'proiezionista', 'bigliettaio')
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
