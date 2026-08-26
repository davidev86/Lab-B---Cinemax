/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import java.time.LocalDate;

import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.UserRequest;

/**
 * Command per la creazione/aggiornamento di un utente.
 * Contiene i dati necessari lato server per inserire o modificare un record utente.
 */
public class StoreUser implements Command, UserRequest{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String username;
	private String md5Password;	
	private String nome;
	private String cognome;
	private LocalDate dataNascita;
	private String domicilio;
	private Ruolo ruolo;
	
	public StoreUser(String username, String md5Password, String nome, String cognome,	 LocalDate dataNascita,	 String domicilio, Ruolo ruolo) {
		this.id = null;
		this.username = username;
		this.md5Password = md5Password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.domicilio = domicilio;
		this.ruolo = ruolo;	
	}
	

	public StoreUser(Integer id, String username, String md5Password, String nome, String cognome,	 LocalDate dataNascita,	 String domicilio,	 Ruolo ruolo) {
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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the md5Password
	 */
	public String getMd5Password() {
		return md5Password;
	}
	/**
	 * @param md5Password the md5Password to set
	 */
	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}
	/**
	 * @param cognome the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	/**
	 * @return the dataNascita
	 */
	public LocalDate getDataNascita() {
		return dataNascita;
	}
	/**
	 * @param dataNascita the dataNascita to set
	 */
	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}
	/**
	 * @return the domicilio
	 */
	public String getDomicilio() {
		return domicilio;
	}
	/**
	 * @param domicilio the domicilio to set
	 */
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	/**
	 * @return the ruolo
	 */
	public Ruolo getRuolo() {
		return ruolo;
	}
	/**
	 * @param ruolo the ruolo to set
	 */
	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
			
}


