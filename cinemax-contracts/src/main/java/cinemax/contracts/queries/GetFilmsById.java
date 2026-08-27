/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.FilmRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per la richiesta dei dettagli di uno specifico film tramite il suo identificatore.
 * <p>
 * Incapsula l'identificativo univoco del film inviato dal client al server 
 * per ottenere le informazioni e i metadati completi associati all'opera.
 * </p>
 */
public class GetFilmsById implements Query, FilmRequest {

    private static final long serialVersionUID = 1L;

    /**
     * Identificatore univoco del film da ricercare nel database.
     */
    private Integer idFilm;
    
    /**
     * Costruttore predefinito senza argomenti.
     */
    public GetFilmsById() {
    }

    /**
     * Crea un'istanza della query specificando l'identificatore del film.
     *
     * @param idFilm
     */
    public GetFilmsById(Integer idFilm) {        
        this.setIdFilm(idFilm);
    }

    /**
     * Restituisce l'identificatore del film target della ricerca.
     *
     * @return l'identificatore del film
     */
    public Integer getIdFilm() {
        return idFilm;
    }

    /**
     * Imposta l'identificatore del film target della ricerca.
     *
     * @param idFilm
     */
    public void setIdFilm(Integer idFilm) {
        this.idFilm = idFilm;
    }
}