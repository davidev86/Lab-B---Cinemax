/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.dto.UserDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta DTO (Data Transfer Object) contenente i dati di dettaglio
 * del profilo di un utente registrato nel sistema Cinemax.
 * <p>
 * Viene utilizzata nello scambio dati via socket TCP dal modulo Server
 * verso il Client a seguito di una richiesta di consultazione profilo.
 * </p>
 */
public class GetUserDetailsResponse implements Response {
    
    /**
     * Identificatore di versione per la serializzazione dell'oggetto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Dettagli completi del profilo dell'utente.
     */
    private UserDetails user;

    /**
     * Costruttore predefinito privo di argomenti.
     * <p>
     * Necessario per consentire la corretta deserializzazione del payload 
     * e per il rispetto delle specifiche JavaBean.
     * </p>
     */
    public GetUserDetailsResponse() {
    }

    /**
     * Costruttore parametrizzato per inizializzare la risposta con il profilo utente.
     *
     * @param user l'oggetto {@link UserDetails} contenente le informazioni dell'utente
     */
    public GetUserDetailsResponse(UserDetails user) {
        this.user = user;
    }

    /**
     * Restituisce i dati di dettaglio dell'utente incapsulati nella risposta.
     *
     * @return l'oggetto {@link UserDetails}, oppure {@code null} se non valorizzato
     */
    public UserDetails getUser() {
        return user;
    }

    /**
     * Imposta i dati di dettaglio dell'utente nella risposta.
     *
     * @param user l'oggetto {@link UserDetails} da associare alla risposta
     */
    public void setUser(UserDetails user) {
        this.user = user;
    }
}