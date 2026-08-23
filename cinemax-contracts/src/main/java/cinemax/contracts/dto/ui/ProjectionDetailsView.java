package cinemax.contracts.dto.ui;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinemax.contracts.dto.ProjectionDetails;

public class ProjectionDetailsView extends ProjectionDetails {
    private static final long serialVersionUID = 1L;

    // SOLO IL DELTA: il campo aggiuntivo per il Frontend
    private Integer totalePostiLiberi;

    public ProjectionDetailsView() {
        super();
    }

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
    public Integer getTotalePostiLiberi() {
        return totalePostiLiberi;
    }

    public void setTotalePostiLiberi(Integer totalePostiLiberi) {
        this.totalePostiLiberi = totalePostiLiberi;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Posti liberi: %d", 
            totalePostiLiberi != null ? totalePostiLiberi : 0);
    }
}