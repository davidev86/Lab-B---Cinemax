package cinemax.contracts.dto;

import java.io.Serializable;
import java.util.Objects;

import cinemax.contracts.dto.Enums.Ruolo;

public class UserMinInfo implements Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id; // Integer per supportare il valore null prima dell'inserimento nel DB
    private String nome;
    private String cognome;
    private String username;   
    private Ruolo ruolo;

    // Costruttore vuoto
    public UserMinInfo() {
    }

    // Costruttore completo
    public UserMinInfo(Integer id, String nome, String cognome, String username, Ruolo ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.ruolo = ruolo;
    }

    // Getters e Setters

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

    public Ruolo getRuolo() {
        return ruolo;
    }

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
