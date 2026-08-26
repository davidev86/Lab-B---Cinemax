/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */


package cinemax.application.services;

import cinemax.contracts.queries.GetFilmsById;
import cinemax.contracts.queries.GetFilmsByTitle;
import cinemax.contracts.responses.GetFilmResponse;
import cinemax.contracts.responses.GetFilmsResponse;

/**
 * Servizio applicativo per la consultazione e la ricerca dei film in catalogo.
 * <p>
 * Incapsula la costruzione delle richieste di tipo Query per il recupero dei dettagli 
 * cinematografici e ne gestisce l'inoltro al backend tramite protocollo TCP.
 */
public class FilmService {

    /** Client TCP per la trasmissione dei messaggi verso il server. */
    private final TcpClient tcpClient;

    /**
     * Costruisce il servizio inizializzando il client di comunicazione TCP.
     *
     * @param tcpClient Il client di rete configurato per l'inoltro delle richieste.
     */
    public FilmService(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    /**
     * Esegue una ricerca nel catalogo film filtrando per titolo.
     *
     * @param titoloFilm Stringa di ricerca o titolo parziale/completo del film.
     * @return Oggetto {@link GetFilmsResponse} contenente la lista dei film corrispondenti ai criteri.
     */
    public GetFilmsResponse getFilmsByTitle(String titoloFilm) {
        GetFilmsByTitle request = new GetFilmsByTitle(titoloFilm);
        return tcpClient.sendRequest(request, GetFilmsResponse.class);
    }

    /**
     * Recupera la scheda informativa completa di un singolo film dato il suo identificativo.
     *
     * @param id Identificativo univoco (ID) del film nel database.
     * @return Oggetto {@link GetFilmResponse} contenente i dettagli del film richiesto.
     */
    public GetFilmResponse getFilmById(Integer id) {
        GetFilmsById request = new GetFilmsById(id);
        return tcpClient.sendRequest(request, GetFilmResponse.class);
    }
}
