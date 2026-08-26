/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.callback;




import cinemax.contracts.dto.UserMinInfo;

/**
 * Callback per notificare esito del processo di autenticazione lato client.
 */
public interface LoginCallBack {

    /**
     * Chiamato quando il login ha successo.
     * @param user informazioni minime dell'utente autenticato
     */
    void onLoginSuccess(UserMinInfo user);

    /**
     * Chiamato quando il login fallisce.
     * @param errorMessage messaggio descrittivo dell'errore
     */
    void onLoginFailed(String errorMessage);

}


