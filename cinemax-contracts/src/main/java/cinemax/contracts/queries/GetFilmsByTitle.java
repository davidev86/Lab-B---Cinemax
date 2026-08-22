package cinemax.contracts.queries;

import cinemax.contracts.interfaces.*;

public class GetFilmsByTitle implements Query, FilmRequest {

    private static final long serialVersionUID = 1L;

    private String titoloFilm;
    
    public GetFilmsByTitle() {
    }

    public GetFilmsByTitle(String titoloFilm) {        
        this.titoloFilm = titoloFilm;
    }

    // Getter e Setter
    public String getTitoloFilm() {
        return titoloFilm;
    }

    public void setTitoloFilm(String titoloFilm) {
        this.titoloFilm = titoloFilm;
    }
}