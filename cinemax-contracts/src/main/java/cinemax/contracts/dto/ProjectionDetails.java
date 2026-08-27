/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) che rappresenta i dettagli completi di una proiezione cinematografica.
 * <p>
 * Aggrega i metadati del film (titolo, genere, regista, durata, limiti di età) alle informazioni
 * di programmazione in sala (data e ora, costo del biglietto e posti attualmente prenotati).
 * Utilizzato per la comunicazione client-server via TCP e per la logica di business.
 * </p>
 */
public class ProjectionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificatore univoco della proiezione nel database.
     */
    private Integer id;

    /**
     * Identificatore univoco del film proiettato.
     */
    private Integer idFilm;

    /**
     * Data e ora pianificate per lo svolgimento della proiezione.
     */
    private LocalDateTime dataOraProiezione;

    /**
     * Titolo dell'opera cinematografica proiettata.
     */
    private String titoloFilm;

    /**
     * Genere cinematografico del film.
     */
    private String genere;

    /**
     * Nome e cognome del regista del film.
     */
    private String regista;

    /**
     * Anno di uscita o produzione del film.
     */
    private Integer anno;

    /**
     * Durata complessiva del film espressa in minuti.
     */
    private Integer durataMinuti;

    /**
     * Soglia di età minima raccomandata o vincolante per la visione.
     */
    private Integer etaMinima;

    /**
     * Prezzo unitario del biglietto per la proiezione.
     */
    private BigDecimal costo;

    /**
     * Numero complessivo di posti già prenotati per la proiezione.
     */
    private Integer totalePostiPrenotati;

    /**
     * Costruttore predefinito senza argomenti.
     */
    public ProjectionDetails() {
    }

    /**
     * Costruisce un'istanza completa di {@code ProjectionDetails} valorizzando tutti i dettagli operativi e descrittivi.
     *
     * @param id                   l'identificatore univoco della proiezione
     * @param idFilm               l'identificatore univoco del film proiettato
     * @param dataOraProiezione    la data e l'ora fissate per la proiezione
     * @param titoloFilm           il titolo del film
     * @param genere               il genere cinematografico
     * @param regista              il nome del regista
     * @param anno                 l'anno di uscita del film
     * @param durataMinuti         la durata della pellicola in minuti
     * @param etaMinima            la soglia di età minima per la visione
     * @param totalePostiPrenotati il numero totale di posti già riservati
     * @param costo                il prezzo unitario del biglietto
     */
    public ProjectionDetails(Integer id, Integer idFilm, LocalDateTime dataOraProiezione, String titoloFilm, String genere, 
                             String regista, Integer anno, Integer durataMinuti, Integer etaMinima, Integer totalePostiPrenotati, BigDecimal costo) {
        this.id = id;
        this.idFilm = idFilm;
        this.dataOraProiezione = dataOraProiezione;
        this.titoloFilm = titoloFilm;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durataMinuti = durataMinuti;
        this.etaMinima = etaMinima;
        this.totalePostiPrenotati = totalePostiPrenotati;
        this.costo = costo;
    }

    /**
     * Restituisce la data e l'ora della proiezione.
     *
     * @return la data e ora come {@link LocalDateTime}
     */
    public LocalDateTime getDataOraProiezione() {
        return dataOraProiezione;
    }

    /**
     * Imposta la data e l'ora della proiezione.
     *
     * @param dataOraProiezione
     */
    public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
        this.dataOraProiezione = dataOraProiezione;
    }

    /**
     * Restituisce il titolo del film associato alla proiezione.
     *
     * @return il titolo del film
     */
    public String getTitoloFilm() {
        return titoloFilm;
    }

    /**
     * Imposta il titolo del film per questa proiezione.
     *
     * @param titoloFilm
     */
    public void setTitoloFilm(String titoloFilm) {
        this.titoloFilm = titoloFilm;
    }

    /**
     * Restituisce il genere cinematografico del film proiettato.
     *
     * @return la categoria o genere del film
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
     * Restituisce il nome del regista del film.
     *
     * @return il nome del regista
     */
    public String getRegista() {
        return regista;
    }

    /**
     * Imposta il nome del regista del film.
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
     * Restituisce la durata complessiva del film in minuti.
     *
     * @return durata in minuti
     */
    public Integer getDurataMinuti() {
        return durataMinuti;
    }

    /**
     * Imposta la durata complessiva del film in minuti.
     *
     * @param durataMinuti
     */
    public void setDurataMinuti(Integer durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    /**
     * Restituisce l'età minima consigliata o obbligatoria per la visione.
     *
     * @return la soglia di età minima
     */
    public Integer getEtaMinima() {
        return etaMinima;
    }

    /**
     * Imposta la soglia di età minima per la visione della proiezione.
     *
     * @param etaMinima
     */
    public void setEtaMinima(Integer etaMinima) {
        this.etaMinima = etaMinima;
    }

    /**
     * Restituisce l'identificatore univoco della proiezione.
     *
     * @return l'identificatore numerico della proiezione
     */
    public Integer getId() {
        return id;
    }

    /**
     * Imposta l'identificatore univoco della proiezione.
     *
     * @param idProiezione
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificatore del film associato alla proiezione.
     *
     * @return l'identificatore del film
     */
    public Integer getIdFilm() {
        return idFilm;
    }

    /**
     * Imposta l'identificatore del film associato alla proiezione.
     *
     * @param idFilm
     */
    public void setIdFilm(Integer idFilm) {
        this.idFilm = idFilm;
    }

    /**
     * Restituisce il costo unitario del biglietto per la proiezione.
     *
     * @return il prezzo del biglietto come {@link BigDecimal}
     */
    public BigDecimal getCosto() {
        return costo;
    }

    /**
     * Imposta il costo unitario del biglietto per la proiezione.
     *
     * @param costo
     */
    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }
    
    /**
     * Restituisce il totale dei posti attualmente prenotati per la proiezione.
     *
     * @return il conteggio dei posti prenotati
     */
    public Integer getTotalePostiPrenotati() {
        return totalePostiPrenotati;
    }

    /**
     * Imposta il conteggio totale dei posti prenotati per la proiezione.
     *
     * @param totalePostiPrenotati
     */
    public void setTotalePostiPrenotati(Integer totalePostiPrenotati) {
        this.totalePostiPrenotati = totalePostiPrenotati;
    }
    
    /**
     * Restituisce una rappresentazione testuale formattata della proiezione.
     * <p>
     * La stringa risultante segue il pattern:
     * {@code "Titolo (Anno) | dd/MM/yyyy HH:mm | X min | €Y.YY"}.
     * </p>
     *
     * @return una stringa formattata contenente i dati essenziali della proiezione
     */
    @Override
    public String toString() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormattata = (dataOraProiezione != null) ? dataOraProiezione.format(formatter) : "Data non disponibile";
        BigDecimal costoVal = (costo != null) ? costo : BigDecimal.ZERO;

        return String.format("%s (%d) | %s | %d min | €%.2f", 
            titoloFilm, 
            anno != null ? anno : 0, 
            dataFormattata, 
            durataMinuti != null ? durataMinuti : 0, 
            costoVal);
    }
}

