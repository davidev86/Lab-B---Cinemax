/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto.ui;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinemax.contracts.dto.ProjectionDetails;

/**
 * Estensione di {@link cinemax.contracts.dto.ProjectionDetails} pensata per il
 * layer di presentazione (frontend). Aggiunge al DTO della proiezione un campo
 * che rappresenta il numero di posti ancora disponibili, utile per mostrare
 * informazioni immediate agli operatori o agli utenti.
 */
public class ProjectionDetailsView extends ProjectionDetails {
    private static final long serialVersionUID = 1L;

    // SOLO IL DELTA: il campo aggiuntivo per il Frontend
    private Integer totalePostiLiberi;

    /**
     * Costruttore vuoto per consentire la serializzazione dell'oggetto via TCP.
     */
    public ProjectionDetailsView() {
        super();
    }

    /**
     * Costruisce una vista completa della proiezione comprensiva del numero di
     * posti liberi calcolato/persistito dal server.
     *
     * @param id identificatore della proiezione
     * @param idFilm identificatore del film
     * @param dataOraProiezione data e ora della proiezione
     * @param titoloFilm titolo del film
     * @param genere genere
     * @param regista regista
     * @param anno anno di uscita
     * @param durataMinuti durata in minuti
     * @param etaMinima etÃ  minima
     * @param costo costo del biglietto
     * @param totalePostiPrenotati posti giÃ  prenotati
     * @param totalePostiLiberi posti ancora disponibili
     */
    public ProjectionDetailsView(Integer id, Integer idFilm, LocalDateTime dataOraProiezione, 
                                 String titoloFilm, String genere, String regista, Integer anno, 
                                 Integer durataMinuti, Integer etaMinima, BigDecimal costo, 
                                 Integer totalePostiPrenotati, Integer totalePostiLiberi) {
        
        // Invocazione del costruttore della classe padre
        super(id, idFilm, dataOraProiezione, titoloFilm, genere, regista, 
              anno, durataMinuti, etaMinima, totalePostiPrenotati, costo);
        
        this.totalePostiLiberi = totalePostiLiberi;
    }

    // Getter e Setter SOLO per il campo nuovo
    /**
     * Restituisce il numero di posti ancora liberi per la proiezione.
     * @return posti liberi (null se non calcolato)
     */
    public Integer getTotalePostiLiberi() {
        return totalePostiLiberi;
    }

    /**
     * Imposta il numero di posti liberi.
     * @param totalePostiLiberi posti liberi
     */
    public void setTotalePostiLiberi(Integer totalePostiLiberi) {
        this.totalePostiLiberi = totalePostiLiberi;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Posti liberi: %d", 
            totalePostiLiberi != null ? totalePostiLiberi : 0);
    }
}


