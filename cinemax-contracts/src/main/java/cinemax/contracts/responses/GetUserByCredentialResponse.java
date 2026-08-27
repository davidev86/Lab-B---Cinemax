/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server a seguito di un tentativo di autenticazione tramite credenziali.
 * <p>
 * Incapsula i dati sintetici di profilo dell'utente ({@link UserMinInfo}) necessari al client
 * per verificare l'avvenuto login e configurare i permessi della sessione applicativa in base al ruolo.
 * </p>
 */
public class GetUserByCredentialResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Informazioni essenziali dell'utente autenticato con successo.
	 */
	private UserMinInfo user;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetUserByCredentialResponse() {
	}

	/**
	 * Costruisce una risposta specificando le informazioni essenziali dell'utente autenticato.
	 *
	 * @param user l'istanza di {@link UserMinInfo} contenente i dati di base dell'utente
	 */
	public GetUserByCredentialResponse(UserMinInfo user) {
		this.user = user;
	}

	/**
	 * Restituisce le informazioni essenziali dell'utente autenticato.
	 *
	 * @return l'oggetto {@link UserMinInfo} associato alla risposta, oppure {@code null} se l'autenticazione è fallita
	 */
	public UserMinInfo getUser() {
		return user;
	}

	/**
	 * Imposta le informazioni essenziali dell'utente autenticato.
	 *
	 * @param user la nuova istanza di {@link UserMinInfo} da associare alla risposta
	 */
	public void setUser(UserMinInfo user) {
		this.user = user;
	}
}