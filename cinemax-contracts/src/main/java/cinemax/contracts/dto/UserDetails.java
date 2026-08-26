/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

import java.io.Serializable;
import java.time.LocalDate;

import cinemax.contracts.dto.Enums.Ruolo;

/**
 * DTO che rappresenta le informazioni dettagliate di un utente,
 * incluse informazioni personali (data di nascita, domicilio) e ruolo.
 * Utilizzato per trasferire dati utente completi tra server e client o
 * per operazioni amministrative lato server.
 */
public class UserDetails implements Serializable  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nome;
	private String cognome;
	private String username;
	private LocalDate dataNascita;
	private String domicilio;
	private Ruolo ruolo;

	/**
	 * Costruttore vuoto per consentire la serializzazione dell'oggetto via TCP.
	 */
	public UserDetails() {
	}

	/**
	 * Costruttore completo (senza id). L'id viene valorizzato dal database dopo l'inserimento.
	 *
	 * @param nome nome
	 * @param cognome cognome
	 * @param username username univoco
	 * @param dataNascita data di nascita
	 * @param domicilio indirizzo/domicilio dell'utente
	 * @param ruolo ruolo assegnato all'utente ({@link cinemax.contracts.dto.Enums.Ruolo})
	 */
	public UserDetails(String nome, String cognome, String username, 
			LocalDate dataNascita, String domicilio, Ruolo ruolo) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.dataNascita = dataNascita;
		this.domicilio = domicilio;
		this.ruolo = ruolo;
	}

	/**
	 * Identificatore dell'utente.
	 * @return id utente
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore dell'utente.
	 * @param id id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce il nome dell'utente.
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Imposta il nome dell'utente.
	 * @param nome nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Restituisce il cognome dell'utente.
	 * @return cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Imposta il cognome.
	 * @param cognome cognome
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * Restituisce l'username dell'utente.
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Imposta l'username.
	 * @param username username univoco
	 */
	public void setUsername(String username) {
		this.username = username;
	} 

	/**
	 * Data di nascita dell'utente.
	 * @return data di nascita
	 */
	public LocalDate getDataNascita() {
		return dataNascita;
	}

	/**
	 * Imposta la data di nascita.
	 * @param dataNascita data di nascita
	 */
	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	/**
	 * Restituisce il domicilio/indirizzo dell'utente.
	 * @return domicilio
	 */
	public String getDomicilio() {
		return domicilio;
	}

	/**
	 * Imposta il domicilio/indirizzo.
	 * @param domicilio domicilio
	 */
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	/**
	 * Restituisce il ruolo assegnato all'utente.
	 * @return ruolo ({@link cinemax.contracts.dto.Enums.Ruolo})
	 */
	public Ruolo getRuolo() {
		return ruolo;
	}

	/**
	 * Imposta il ruolo dell'utente.
	 * @param ruolo ruolo
	 */
	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
}


