/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.FilmRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per cercare film per titolo (pattern). Usata per ricerche lato server.
 */
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

