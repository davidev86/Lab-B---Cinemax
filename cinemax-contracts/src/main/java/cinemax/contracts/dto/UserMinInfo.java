/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

import java.io.Serializable;
import java.util.Objects;

import cinemax.contracts.dto.Enums.Ruolo;

/**
 * DTO minimale che rappresenta le informazioni base di un utente utilizzate
 * nelle callback e nelle risposte lato client (username, nome, cognome e ruolo).
 */
public class UserMinInfo implements Serializable  {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco nel database.
	 * <p>
	 * È di tipo {@link Integer} per consentire il valore {@code null} prima della persistenza nel database.
	 * </p>
	 */
	private Integer id;

	/**
	 * Nome anagrafico dell'utente.
	 */
	private String nome;

	/**
	 * Cognome anagrafico dell'utente.
	 */
	private String cognome;

	/**
	 * Nome utente (username) univoco per l'autenticazione nel sistema.
	 */
	private String username;

	/**
	 * Ruolo autorizzativo assegnato all'utente nel sistema (es. {@link Ruolo}).
	 */
	private Ruolo ruolo;

    /**
     * Costruttore vuoto per consentire la serializzazione dell'oggetto via TCP.
     */
    public UserMinInfo() {
    }

    /**
     * Costruisce un UserMinInfo con i campi principali.
     * @param id identificatore dell'utente (null se non ancora assegnato)
     * @param nome nome
     * @param cognome cognome
     * @param username username univoco
     * @param ruolo ruolo dell'utente (es. CLIENTE, BIGLIETTAIO)
     */
    public UserMinInfo(Integer id, String nome, String cognome, String username, Ruolo ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.ruolo = ruolo;
    }

    // Getters e Setters

    /**
     * Identificatore dell'utente.
     * @return id utente (null se non disponibile)
     */
    public Integer getId() {
        return id;
    }

    /**
     * Imposta l'identificatore dell'utente.
     * @param id id utente
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
     * Imposta il cognome dell'utente.
     * @param cognome cognome
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce l'username univoco dell'utente.
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
     * Restituisce il ruolo assegnato all'utente.
     * @return ruolo (enum {@link cinemax.contracts.dto.Enums.Ruolo})
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMinInfo utente = (UserMinInfo) o;
        return Objects.equals(id, utente.id) || Objects.equals(username, utente.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +               
                ", ruolo=" + ruolo +
                '}';
    }
}


