/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta che comunica il successo dell'operazione di prenotazione con ID generato dal database.
 */
public class StoreBookingResponse implements Response {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private boolean success;

    // Costruttore vuoto per serializzazione 
    public StoreBookingResponse() {
    }

    // Costruttore per successo
    public StoreBookingResponse(Integer id) {
        this.id = id;
        this.success = true;
    }

    // Costruttore per definire esplicitamente lo stato
    public StoreBookingResponse(Integer id, boolean success) {
        this.id = id;
        this.success = success;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSuccess() { 
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}


