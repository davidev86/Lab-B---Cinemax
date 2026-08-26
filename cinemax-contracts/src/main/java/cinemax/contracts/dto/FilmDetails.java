/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

import java.io.Serializable;

/**
 * DTO che rappresenta le informazioni principali di un film.
 * Usato dal server per trasferire i metadati di un film al client (titolo, genere,
 * regista, anno di uscita, durata e vincolo di età ).
 * <p>
 * è previsto un costruttore vuoto per il meccanismo di marshalling/unmarshalling
 * usato nella comunicazione client-server via socket TCP e un costruttore completo
 * per facilità  di creazione nelle parti di business logic o nei test.
 */
public class FilmDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String titoloFilm;
    private String genere;
    private String regista;
    private Integer anno;
    private Integer durataMinuti;
    private Integer etaMinima;

    /**
     * Costruttore vuoto per consentire la serializzazione dell'oggetto via TCP.
     */
    public FilmDetails() {
    }

    /**
     * Costruisce un FilmDetails completo.
     *
     * @param id identificatore del film (null se non persistito)
     * @param titoloFilm titolo del film
     * @param genere genere/etichetta del film
     * @param regista nome del regista
     * @param anno anno di produzione/uscita
     * @param durataMinuti durata in minuti
     * @param etaMinima età  minima consigliata (VM)
     */
    public FilmDetails(Integer id, String titoloFilm, String genere, String regista, Integer anno, Integer durataMinuti, Integer etaMinima) {
        this.id = id;
        this.titoloFilm = titoloFilm;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durataMinuti = durataMinuti;
        this.etaMinima = etaMinima;
    }

    // Getter e Setter
    /**
     * Identificatore del film (null se non ancora persistito).
     * @return id del film
     */
    public Integer getId() {
        return id;
    }

    /**
     * Imposta l'identificatore del film.
     * @param id id del film
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Titolo del film.
     * @return titolo
     */
    public String getTitoloFilm() {
        return titoloFilm;
    }

    /**
     * Imposta il titolo del film.
     * @param titoloFilm titolo
     */
    public void setTitoloFilm(String titoloFilm) {
        this.titoloFilm = titoloFilm;
    }

    /**
     * Genere del film (es. "Commedia", "Drammatico").
     * @return genere
     */
    public String getGenere() {
        return genere;
    }

    /**
     * Imposta il genere del film.
     * @param genere genere
     */
    public void setGenere(String genere) {
        this.genere = genere;
    }

    /**
     * Nome del regista.
     * @return regista
     */
    public String getRegista() {
        return regista;
    }

    /**
     * Imposta il regista.
     * @param regista nome del regista
     */
    public void setRegista(String regista) {
        this.regista = regista;
    }

    /**
     * Anno di uscita/produzione del film.
     * @return anno (null se non specificato)
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * Imposta l'anno di produzione/uscita.
     * @param anno anno
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * Durata del film in minuti.
     * @return durata in minuti
     */
    public Integer getDurataMinuti() {
        return durataMinuti;
    }

    /**
     * Imposta la durata del film in minuti.
     * @param durataMinuti durata in minuti
     */
    public void setDurataMinuti(Integer durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    /**
     * Limite di età  consigliato/obbligatorio (es. 14, 18) per la visione del film.
     * @return età  minima consigliata
     */
    public Integer getEtaMinima() {
        return etaMinima;
    }

    /**
     * Imposta il vincolo di età  minima per la visione.
     * @param etaMinima età  minima
     */
    public void setEtaMinima(Integer etaMinima) {
        this.etaMinima = etaMinima;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) | %s | Regia: %s | %d min | VM%d",
            titoloFilm != null ? titoloFilm : "Titolo non disponibile",
            anno != null ? anno : 0,
            genere != null ? genere : "Genere N.D.",
            regista != null ? regista : "Sconosciuto",
            durataMinuti != null ? durataMinuti : 0,
            etaMinima != null ? etaMinima : 0);
    }
}


