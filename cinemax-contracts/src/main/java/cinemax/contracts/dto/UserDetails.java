package cinemax.contracts.dto;

import java.io.Serializable;
import java.time.LocalDate;

import cinemax.contracts.dto.Enums.Ruolo;

public class UserDetails implements Serializable  {


	private Integer id;
	private String nome;
	private String cognome;
	private String username;
	private LocalDate dataNascita;
	private String domicilio;
	private Ruolo ruolo;

	// Costruttore vuoto (richiesto da JPA)
	public UserDetails() {
	}

	// Costruttore completo senza ID (l'ID viene generato dal DB)
	public UserDetails(String nome, String cognome, String username, 
			LocalDate dataNascita, String domicilio, Ruolo ruolo) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.dataNascita = dataNascita;
		this.domicilio = domicilio;
		this.ruolo = ruolo;
	}

	// --- GETTER e SETTER ---

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	 

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
}
