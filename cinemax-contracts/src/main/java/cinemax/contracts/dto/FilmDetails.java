/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

import java.io.Serializable;

/**

 * Data Transfer Object (DTO) contenente i dettagli descrittivi di un film.
 * <p>
 * Utilizzato per trasferire i metadati dell'opera cinematografica (titolo, genere,
 * regista, anno di uscita, durata e vincoli di età) dal server al client
 * tramite protocollo TCP.
 * </p>

 */
public class FilmDetails implements Serializable { 
    
    private static final long serialVersionUID = 1L;

    /**
     * Identificatore univoco del film nel database.
     */
    private Integer id;

    /**
     * Titolo dell'opera cinematografica.
     */
    private String titoloFilm;

    /**
     * Genere cinematografico (es. "Commedia", "Drammatico", "Azione").
     */
    private String genere;

    /**
     * Nome e cognome del regista.
     */
    private String regista;

    /**
     * Anno di produzione o uscita nelle sale.
     */
    private Integer anno;

    /**
     * Durata complessiva della pellicola espressa in minuti.
     */
    private Integer durataMinuti;

    /**
     * Soglia di età minima raccomandata o vincolante per la visione.
     */
    private Integer etaMinima;

    /**
     * Costruttore predefinito senza argomenti.
     */
    public FilmDetails() {
    }

    /**
     * Costruisce un'istanza completa di {@code FilmDetails} valorizzando tutti i campi informativi.
     *

     * @param id           l'identificatore univoco del film
     * @param titoloFilm   il titolo del film
     * @param genere       il genere cinematografico
     * @param regista      il nome del regista
     * @param anno         l'anno di produzione o uscita
     * @param durataMinuti la durata in minuti
     * @param etaMinima    la soglia di età minima per la visione

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

    /**
     * Restituisce l'identificatore univoco del film.
     *
     * @return l'identificatore del film
     */
    public Integer getId() {
        return id;
    }

    /**
     * Imposta l'identificatore univoco del film.
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Restituisce il titolo del film.
     *
     * @return il titolo del film
     */
    public String getTitoloFilm() {
        return titoloFilm;
    }

    /**
     * Imposta il titolo del film.
     *
     * @param titoloFilm
     */
    public void setTitoloFilm(String titoloFilm) {
        this.titoloFilm = titoloFilm;
    }

    /**
     * Restituisce il genere cinematografico del film.
     *
     * @return il genere del film
     */
    public String getGenere() {
        return genere;
    }

    /**
     * Imposta il genere cinematografico del film.
     *
     * @param genere
     */
    public void setGenere(String genere) {
        this.genere = genere;
    }

    /**
     * Restituisce il nome del regista.
     *
     * @return il nome del regista
     */
    public String getRegista() {
        return regista;
    }

    /**
     * Imposta il nome del regista.
     *
     * @param regista
     */
    public void setRegista(String regista) {
        this.regista = regista;
    }

    /**
     * Restituisce l'anno di uscita o produzione del film.
     *
     * @return l'anno del film, oppure null se non disponibile
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * Imposta l'anno di uscita o produzione del film.
     *
     * @param anno
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * Restituisce la durata del film espressa in minuti.
     *
     * @return la durata in minuti
     */
    public Integer getDurataMinuti() {
        return durataMinuti;
    }

    /**
     * Imposta la durata del film in minuti.
     *
     * @param durataMinuti
     */
    public void setDurataMinuti(Integer durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    /**

     * Restituisce l'età minima consigliata o obbligatoria per la visione del film.
     *
     * @return la soglia di età minima (es. 0 per tutti, 14, 18)

     */
    public Integer getEtaMinima() {
        return etaMinima;
    }

    /**

     * Imposta l'età minima consigliata o obbligatoria per la visione del film.
     *
     * @param etaMinima

     */
    public void setEtaMinima(Integer etaMinima) {
        this.etaMinima = etaMinima;
    }

    /**
     * Restituisce una rappresentazione testuale formattata dei metadati del film.
     * <p>
     * La stringa risultante adotta il pattern:
     * {@code "Titolo (Anno) | Genere | Regia: Regista | X min | VMY"}.
     * Vengono applicati valori di fallback predefiniti in caso di attributi nulli.
     * </p>
     *
     * @return una stringa formattata riepilogativa del film
     */
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