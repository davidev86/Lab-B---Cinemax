package cinemax.contracts.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


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

    // Costruttore vuoto (indispensabile per la serializzazione/deserializzazione)
    public ProjectionDetails() {
    }

    // Costruttore con campi (opzionale ma comodo)
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdFilm() {
		return idFilm;
	}

	public void setIdFilm(Integer idFilm) {
		this.idFilm = idFilm;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}
	
	public Integer getTotalePostiPrenotati() {
		return totalePostiPrenotati;
	}

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