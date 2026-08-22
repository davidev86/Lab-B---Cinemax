package cinemax.contracts.dto;

import java.io.Serializable;

public class FilmDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String titoloFilm;
    private String genere;
    private String regista;
    private Integer anno;
    private Integer durataMinuti;
    private Integer etaMinima;

    public FilmDetails() {
    }

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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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