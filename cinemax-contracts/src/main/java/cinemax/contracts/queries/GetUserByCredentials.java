/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.UserRequest;

/**
 * Query parametrizzata per l'autenticazione di un utente nel sistema.
 * <p>
 * Incapsula le credenziali di accesso (username e password cifrata tramite hash MD5)
 * inviate dal client al server per verificare l'identità dell'utente e recuperarne i dati di sessione.
 * </p>
 */
public class GetUserByCredentials implements Query, UserRequest {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Nome utente (username) fornito per l'autenticazione.
	 */
	private String username;	

	/**
	 * Password dell'utente codificata in formato hash MD5.
	 */
	private String md5Password;

	/**
	 * Costruttore predefinito senza argomenti.

	 */
	public GetUserByCredentials() {
	}
	
	/**
	 * Costruisce un'istanza della query specificando le credenziali di accesso.
	 *
	 * @param username    il nome utente per l'autenticazione
	 * @param md5Password la password dell'utente già cifrata in formato MD5
	 */
	public GetUserByCredentials(String username, String md5Password) {
		this.username = username;
		this.md5Password = md5Password;
	}

	/**
	 * Restituisce il nome utente specificato per il login.
	 *
	 * @return il nome utente (username)
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Imposta il nome utente per il login.
	 *
	 * @param username il nuovo nome utente
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Restituisce l'hash MD5 della password inserita.
	 *
	 * @return la stringa hash MD5 della password
	 */
	public String getMd5Password() {
		return md5Password;
	}

	/**
	 * Imposta l'hash MD5 della password.
	 *
	 * @param md5Password la nuova stringa hash MD5 della password
	 */
	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}
}


