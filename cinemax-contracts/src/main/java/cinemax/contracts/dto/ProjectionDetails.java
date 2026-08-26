/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * DTO che rappresenta i dettagli di una proiezione: collega i metadati del film
 * alle informazioni di scheduling (data/ora), costo e posti prenotati.
 * <p>
 * Utilizzato sia dal server per inviare informazioni al client sia internamente
 * per la logica di business relativa alla gestione delle proiezioni.
 */
public class ProjectionDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer idFilm;
    private LocalDateTime dataOraProiezione;
    private String titoloFilm;
    private String genere;
    private String regista;
    private Integer anno;
    private Integer durataMinuti;
    private Integer etaMinima;
    private BigDecimal costo;
    private Integer totalePostiPrenotati;

    /**
     * Costruttore vuoto per consentire la serializzazione dell'oggetto via TCP.
     */
    public ProjectionDetails() {
    }

    /**
     * Costruisce un ProjectionDetails completo con i campi principali.
     *
     * @param id identificatore della proiezione
     * @param idFilm identificatore del film proiettato
     * @param dataOraProiezione data e ora della proiezione
     * @param titoloFilm titolo del film
     * @param genere genere del film
     * @param regista regista del film
     * @param anno anno di uscita
     * @param durataMinuti durata del film in minuti
     * @param etaMinima etÃ  minima consigliata
     * @param totalePostiPrenotati numero di posti giÃ  prenotati per la proiezione
     * @param costo costo unitario del biglietto per la proiezione
     */
    public ProjectionDetails(Integer id, Integer idFilm, LocalDateTime dataOraProiezione, String titoloFilm, String genere, 
                                    String regista, Integer anno, Integer durataMinuti, Integer etaMinima, Integer totalePostiPrenotati, BigDecimal costo ) {
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

    // Getter e Setter
    /**
     * Data e ora della proiezione.
     * @return data/ora come {@link LocalDateTime}
     */
    public LocalDateTime getDataOraProiezione() {
        return dataOraProiezione;
    }

    /**
     * Imposta la data e ora della proiezione.
     * @param dataOraProiezione data e ora
     */
    public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
        this.dataOraProiezione = dataOraProiezione;
    }

    /**
     * Titolo del film associato alla proiezione.
     * @return titolo del film
     */
    public String getTitoloFilm() {
        return titoloFilm;
    }

    /**
     * Imposta il titolo del film per questa proiezione.
     * @param titoloFilm titolo
     */
    public void setTitoloFilm(String titoloFilm) {
        this.titoloFilm = titoloFilm;
    }

    /**
     * Genere del film per questa proiezione.
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
     * Regista del film.
     * @return nome del regista
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
     * Anno di produzione/uscita.
     * @return anno
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
     * Imposta la durata in minuti.
     * @param durataMinuti durata
     */
    public void setDurataMinuti(Integer durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    /**
     * EtÃ  minima consigliata/obbligatoria per la proiezione.
     * @return etÃ  minima
     */
    public Integer getEtaMinima() {
        return etaMinima;
    }

    /**
     * Imposta l'etÃ  minima consigliata/obbligatoria.
     * @param etaMinima etÃ  minima
     */
    public void setEtaMinima(Integer etaMinima) {
        this.etaMinima = etaMinima;
    }

    /**
     * Identificatore della proiezione.
     * @return id proiezione
     */
    public Integer getId() {
        return id;
    }

    /**
     * Imposta l'identificatore della proiezione.
     * @param id id proiezione
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Identificatore del film associato alla proiezione.
     * @return id film
     */
    public Integer getIdFilm() {
        return idFilm;
    }

    /**
     * Imposta l'identificatore del film.
     * @param idFilm id film
     */
    public void setIdFilm(Integer idFilm) {
        this.idFilm = idFilm;
    }

    /**
     * Costo del biglietto per la proiezione.
     * @return costo come {@link BigDecimal}
     */
    public BigDecimal getCosto() {
        return costo;
    }

    /**
     * Imposta il costo del biglietto.
     * @param costo costo
     */
    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }
	
    /**
     * Numero totale di posti giÃ  prenotati per la proiezione.
     * @return numero di posti prenotati
     */
    public Integer getTotalePostiPrenotati() {
        return totalePostiPrenotati;
    }

    /**
     * Imposta il numero totale di posti prenotati.
     * @param totalePostiPrenotati numero di posti
     */
    public void setTotalePostiPrenotati(Integer totalePostiPrenotati) {
        this.totalePostiPrenotati = totalePostiPrenotati;
    }
	
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


