/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.FilmRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per la ricerca di film in base al titolo (o pattern di testo).
 * <p>
 * Incapsula il criterio di ricerca testuale inviato dal client al server 
 * per ottenere l'elenco delle opere cinematografiche corrispondenti.
 * </p>
 */
public class GetFilmsByTitle implements Query, FilmRequest {

    private static final long serialVersionUID = 1L;

    /**
     * Titolo o porzione di titolo del film da ricercare.
     */
    private String titoloFilm;
    
    /**
     * Costruttore predefinito senza argomenti.
     
     */
    public GetFilmsByTitle() {
    }

    /**
     * Crea un'istanza della query specificando il titolo (o criterio testuale) da ricercare.
     *
     * @param titoloFilm il titolo o porzione di esso da impostare come filtro
     */
    public GetFilmsByTitle(String titoloFilm) {        
        this.titoloFilm = titoloFilm;
    }

    /**
     * Restituisce il titolo del film impostato come filtro di ricerca.
     *
     * @return il titolo del film, oppure {@code null} se non specificato
     */
    public String getTitoloFilm() {
        return titoloFilm;
    }

    /**
     * Imposta il titolo del film (o criterio testuale) da ricercare.
     *
     * @param titoloFilm
     */
    public void setTitoloFilm(String titoloFilm) {
        this.titoloFilm = titoloFilm;
    }
}