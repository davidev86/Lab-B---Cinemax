/*
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */


package cinemax.application.services;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import cinemax.application.services.utils.HashBuilder;
import cinemax.contracts.commands.StoreUser;
import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.queries.GetUserByCredentials;
import cinemax.contracts.queries.GetUserDetails;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.contracts.responses.GetUserDetailsResponse;
import cinemax.contracts.responses.StoreUserResponse;

/**
 * Servizio per la gestione degli account utente e dell'autenticazione.
 * <p>
 * Si occupa della cifratura delle credenziali tramite hashing MD5, della registrazione 
 * di nuovi profili, dell'aggiornamento dei dati anagrafici e del recupero delle 
 * informazioni utente tramite canale di comunicazione TCP.
 */
public class UserService {

    /** Client TCP per la trasmissione dei messaggi verso il server. */
    private final TcpClient tcpClient;

    /**
     * Costruisce il servizio associando il client TCP per la comunicazione di rete.
     *
     * @param tcpClient Il client di rete configurato per inoltrare le richieste.
     */
    public UserService(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

<<<<<<< Updated upstream
		
	public StoreUserResponse insertUser(String username, String password, String nome, String cognome, LocalDate dataNascita, String domicilio) {
	    try {
	        String md5Password = HashBuilder.convertToMD5(password);
	        StoreUser request = new StoreUser(username, md5Password, nome, cognome, dataNascita, domicilio, Ruolo.CLIENTE);        
	        
	        StoreUserResponse response = tcpClient.sendRequest(request, StoreUserResponse.class);
	        
	        // Verifica se la risposta è valida e se l'operazione ha avuto successo
	        if (response == null || response.getId() == null) {
	            String msg = "Utente duplicato!";
	            throw new RuntimeException(msg);
	        }
	        
	        return response;

	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Errore nell'algoritmo di hashing della password.", e);
	    }
	}	
}
=======
    /**
     * Esegue l'autenticazione dell'utente verificando la corrispondenza di username e password.
     * <p>
     * La password in chiaro viene convertita in hash MD5 prima di comporre la richiesta verso il server.
     *
     * @param username Nome utente inserito in fase di login.
     * @param password Password in chiaro inserita dall'utente.
     * @return Oggetto {@link GetUserByCredentialResponse} contenente l'esito del login e i dati profilo,
     *         oppure {@code null} se l'algoritmo di hashing MD5 non è disponibile nell'ambiente.
     */
    public GetUserByCredentialResponse getUserByCredentials(String username, String password) {
        try {
            String md5Passord = HashBuilder.convertToMD5(password);
            GetUserByCredentials request = new GetUserByCredentials(username, md5Passord);
            return tcpClient.sendRequest(request, GetUserByCredentialResponse.class);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Recupera le informazioni anagrafiche e di profilo complete di un utente dato il suo ID.
     *
     * @param userId Identificativo univoco dell'utente nel database.
     * @return Oggetto {@link GetUserDetailsResponse} con i dettagli del profilo richiesto.
     */
    public GetUserDetailsResponse getUserDetails(int userId) {
        GetUserDetails request = new GetUserDetails(userId);
        return tcpClient.sendRequest(request, GetUserDetailsResponse.class);
    }

    /**
     * Aggiorna i dati anagrafici, le credenziali e i permessi di un utente già registrato.
     *
     * @param id          Identificativo univoco dell'utente da modificare.
     * @param username    Nuovo username associato all'account.
     * @param password    Nuova password in chiaro (verrà convertita in hash MD5).
     * @param nome        Nome dell'utente.
     * @param cognome     Cognome dell'utente.
     * @param dataNascita Data di nascita dell'utente.
     * @param domicilio   Indirizzo o comune di domicilio.
     * @param ruolo       Livello di autorizzazione assegnato (es. {@link Ruolo}).
     * @return Oggetto {@link StoreUserResponse} con l'esito dell'aggiornamento, 
     *         oppure {@code null} in caso di fallimento della cifratura MD5.
     */
    public StoreUserResponse updateUser(Integer id, String username, String password, String nome, 
                                       String cognome, LocalDate dataNascita, String domicilio, Ruolo ruolo) {
        try {
            String md5Passord = HashBuilder.convertToMD5(password);
            StoreUser request = new StoreUser(id, username, md5Passord, nome, cognome, dataNascita, domicilio, ruolo);
            return tcpClient.sendRequest(request, StoreUserResponse.class);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Inserisce un nuovo utente a sistema assegnandogli automaticamente il ruolo base {@link Ruolo#CLIENTE}.
     *
     * @param username    Username scelto per il nuovo account.
     * @param password    Password in chiaro (verrà convertita in hash MD5).
     * @param nome        Nome anagrafico del cliente.
     * @param cognome     Cognome anagrafico del cliente.
     * @param dataNascita Data di nascita del cliente (può essere {@code null}).
     * @param domicilio   Indirizzo di residenza/domicilio del cliente.
     * @return Oggetto {@link StoreUserResponse} con l'esito della registrazione, 
     *         oppure {@code null} in caso di fallimento della cifratura MD5.
     */
    public StoreUserResponse insertUser(String username, String password, String nome, 
                                       String cognome, LocalDate dataNascita, String domicilio) {
        try {
            String md5Passord = HashBuilder.convertToMD5(password);
            StoreUser request = new StoreUser(username, md5Passord, nome, cognome, dataNascita, domicilio, Ruolo.CLIENTE);
            return tcpClient.sendRequest(request, StoreUserResponse.class);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
>>>>>>> Stashed changes
