/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import cinemax.contracts.interfaces.FilmRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per richiedere i dettagli di un film tramite il suo identificatore.
 */
public class GetFilmsById implements Query, FilmRequest {

    private static final long serialVersionUID = 1L;

    private Integer idFilm;
    
    public GetFilmsById() {
    }

    public GetFilmsById(Integer idFilm) {        
        this.setIdFilm(idFilm);
    }

	public Integer getIdFilm() {
		return idFilm;
	}

	public void setIdFilm(Integer idFilm) {
		this.idFilm = idFilm;
	}

    // Getter e Setter
  
}

