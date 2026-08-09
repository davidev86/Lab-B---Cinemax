package cinemax.contracts.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Projection implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime dataOraProiezione;
    private String titoloFilm;
    private String genere;
    private String regista;
    private Integer anno;
    private Integer durataMinuti;
    private Integer etaMinima;

    // Costruttore vuoto (indispensabile per la serializzazione/deserializzazione)
    public Projection() {
    }

    // Costruttore con campi (opzionale ma comodo)
    public Projection(LocalDateTime dataOraProiezione, String titoloFilm, String genere, 
                                    String regista, Integer anno, Integer durataMinuti, Integer etaMinima) {
        this.dataOraProiezione = dataOraProiezione;
        this.titoloFilm = titoloFilm;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durataMinuti = durataMinuti;
        this.etaMinima = etaMinima;
    }

    // Getter e Setter
    public LocalDateTime getDataOraProiezione() {
        return dataOraProiezione;
    }

    public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
        this.dataOraProiezione = dataOraProiezione;
    }

    public String getTitoloFilm() {
        return titoloFilm;
    }

    public void setTitoloFilm(String titoloFilm) {
        this.titoloFilm = titoloFilm;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getRegista() {
        return regista;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getDurataMinuti() {
        return durataMinuti;
    }

    public void setDurataMinuti(Integer durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    public Integer getEtaMinima() {
        return etaMinima;
    }

    public void setEtaMinima(Integer etaMinima) {
        this.etaMinima = etaMinima;
    }
}