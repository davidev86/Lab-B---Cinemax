/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import java.time.LocalDate;

import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.UserRequest;

/**
 * Comando per la creazione o l'aggiornamento di un utente nel sistema.
 * <p>
 * Incapsula le credenziali di accesso, i dati anagrafici e il ruolo
 * necessari per l'elaborazione e la persistenza del record utente lato server.
 * </p>
 * 
 * @author Francesca Pelizzoni (Matricola 751550 - VA)
 * @author Davide Villa (Matricola 701105 - VA)
 */
public class StoreUser implements Command, UserRequest { 
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identificatore univoco dell'utente (impostato per modifiche, {@code null} per nuovi inserimenti).
	 */
	private Integer id;

	/**
	 * Nome utente (username) univoco per l'autenticazione.
	 */
	private String username;

	/**
	 * Password dell'utente codificata con hash MD5.
	 */
	private String md5Password;	

	/**
	 * Nome anagrafico dell'utente.
	 */
	private String nome;

	/**
	 * Cognome anagrafico dell'utente.
	 */
	private String cognome;

	/**
	 * Data di nascita dell'utente.
	 */
	private LocalDate dataNascita;

	/**
	 * Indirizzo di domicilio o residenza dell'utente.
	 */
	private String domicilio;

	/**
	 * Ruolo assegnato all'utente nel sistema (es. CLIENTE, AMMINISTRATORE).
	 */
	private Ruolo ruolo;
	
	/**
	 * Costruttore per la creazione di un nuovo utente (inserimento).
	 * <p>
	 * L'identificativo viene impostato a {@code null} poiché verrà generato automaticamente dal database.
	 * </p>
	 *
	 * @param username    il nome utente per l'accesso
	 * @param md5Password la password già cifrata in formato hash MD5
	 * @param nome        il nome anagrafico
	 * @param cognome     il cognome anagrafico
	 * @param dataNascita la data di nascita
	 * @param domicilio   l'indirizzo di domicilio
	 * @param ruolo       il ruolo autorizzativo assegnato
	 */
	public StoreUser(String username, String md5Password, String nome, String cognome, LocalDate dataNascita, String domicilio, Ruolo ruolo) {
		this.id = null;
		this.username = username;
		this.md5Password = md5Password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.domicilio = domicilio;
		this.ruolo = ruolo;	
	}
	
	/**
	 * Costruttore per la modifica di un utente esistente (aggiornamento).
	 *
	 * @param id          l'identificatore univoco dell'utente da aggiornare
	 * @param username    il nome utente per l'accesso
	 * @param md5Password la password aggiornata già cifrata in formato hash MD5
	 * @param nome        il nome anagrafico
	 * @param cognome     il cognome anagrafico
	 * @param dataNascita la data di nascita
	 * @param domicilio   l'indirizzo di domicilio
	 * @param ruolo       il ruolo autorizzativo assegnato
	 */
	public StoreUser(Integer id, String username, String md5Password, String nome, String cognome, LocalDate dataNascita, String domicilio, Ruolo ruolo) {
		this.id = id;
		this.username = username;
		this.md5Password = md5Password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.domicilio = domicilio;
		this.ruolo = ruolo;	
	}
	
	/**
	 * Restituisce l'identificatore univoco dell'utente.
	 *
	 * @return l'identificatore dell'utente, oppure null se non disponibile
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore univoco dell'utente.
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce il nome utente.
	 *
	 * @return il nome utente per il login
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Imposta il nome utente.
	 *
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Restituisce l'hash MD5 della password.
	 *
	 * @return la password cifrata in MD5
	 */
	public String getMd5Password() {
		return md5Password;
	}

	/**
	 * Imposta l'hash MD5 della password.
	 *
	 * @param md5Password
	 */
	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}

	/**
	 * Restituisce il nome dell'utente.
	 *
	 * @return il nome anagrafico
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Imposta il nome dell'utente.
	 *
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Restituisce il cognome dell'utente.
	 *
	 * @return il cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Imposta il cognome dell'utente.
	 *
	 * @param cognome
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * Restituisce la data di nascita dell'utente.
	 *
	 * @return la data di nascita come {@link LocalDate}
	 */
	public LocalDate getDataNascita() {
		return dataNascita;
	}

	/**
	 * Imposta la data di nascita dell'utente.
	 *
	 * @param dataNascita
	 */
	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	/**
	 * Restituisce il domicilio dell'utente.
	 *
	 * @return l'indirizzo di domicilio
	 */
	public String getDomicilio() {
		return domicilio;
	}

	/**
	 * Imposta il domicilio dell'utente.
	 *
	 * @param domicilio
	 */
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	/**
	 * Restituisce il ruolo dell'utente all'interno del sistema.
	 *
	 * @return il ruolo come enum {@link Ruolo}
	 */
	public Ruolo getRuolo() {
		return ruolo;
	}

	/**
	 * Imposta il ruolo dell'utente all'interno del sistema.
	 *
	 * @param ruolo
	 */
	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
}