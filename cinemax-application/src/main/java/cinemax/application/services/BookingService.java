/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.application.services;

import java.time.LocalDate;

import cinemax.contracts.commands.DeleteBooking;
import cinemax.contracts.commands.StoreBooking;
import cinemax.contracts.queries.GetBookings;
import cinemax.contracts.queries.GetBookingsByDate;
import cinemax.contracts.queries.GetBookingsByUserId;
import cinemax.contracts.responses.DeleteBookingResponse;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.StoreBookingResponse;

/**
 * Servizio per la gestione del ciclo di vita delle prenotazioni.
 * <p>
 * Agisce come intermediario verso il backend, incapsulando la costruzione 
 * dei messaggi di comando (Commands) e di interrogazione (Queries) e 
 * inoltrandoli tramite il canale di comunicazione TCP.
 */
public class BookingService {

    /** Client TCP per la trasmissione dei messaggi verso il server. */
    private final TcpClient tcpClient;

    /**
     * Costruisce il servizio inizializzando il client di rete sottostante.
     *
     * @param tcpClient Il client TCP configurato per l'invio delle richieste.
     */
    public BookingService(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    /**
     * Recupera le prenotazioni filtrandole in base ai criteri specificati.
     * I parametri nulli vengono ignorati durante il filtraggio.
     *
     * @param codicePrenotazione Identificativo numerico della singola prenotazione.
     * @param nomeCliente        Nome dell'utente di tipo Cliente.
     * @param cognomeCliente     Cognome dell'utente di tipo Cliente.
     * @param titoloFilm         Titolo del film associato alla proiezione.
     * @param daDataProiezione   Data iniziale dell'intervallo di ricerca proiezioni.
     * @param aDataProiezione    Data finale dell'intervallo di ricerca proiezioni.
     * @return Oggetto {@link GetBookingResponse} contenente l'elenco dei record corrispondenti.
     */
    public GetBookingResponse getBookings(Integer codicePrenotazione, String nomeCliente, String cognomeCliente, 
                                         String titoloFilm, LocalDate daDataProiezione, LocalDate aDataProiezione) {
        
        GetBookings request = new GetBookings(codicePrenotazione, nomeCliente, cognomeCliente, titoloFilm, daDataProiezione, aDataProiezione);
        return tcpClient.sendRequest(request, GetBookingResponse.class);
    }

    /**
     * Recupera le prenotazioni appartenenti a un determinato utente.
     *
     * @param idUtente Identificativo univoco dell'utente.
     * @return Oggetto {@link GetBookingResponse} contenente la lista delle prenotazioni effettuate.
     */
    public GetBookingResponse getBookingsByUserId(Integer idUtente) {
        
        GetBookingsByUserId request = new GetBookingsByUserId(idUtente);
        return tcpClient.sendRequest(request, GetBookingResponse.class);
    }
    
    /**
     * Recupera le prenotazioni di oggi.
     *
     * @param No params
     * @return Oggetto {@link GetBookingResponse} contenente la lista delle prenotazioni effettuate.
     */
    public GetBookingResponse getBookingsCurrentDay() {
        
    	GetBookingsByDate request = new GetBookingsByDate(LocalDate.now());
        return tcpClient.sendRequest(request, GetBookingResponse.class);
    }

    /**
     * Aggiorna i dati di una prenotazione precedentemente salvata.
     *
     * @param id           Codice identificativo della prenotazione da modificare.
     * @param idUtente     Identificativo dell'utente di tipo cliente.
     * @param idProiezione Identificativo della proiezione selezionata.
     * @param numeroPosti  Nuovo quantitativo di posti riservati.
     * @return Oggetto {@link StoreBookingResponse} con l'esito dell'operazione.
     */
    public StoreBookingResponse updateBooking(Integer id, Integer idUtente, Integer idProiezione, Integer numeroPosti) {
        
        StoreBooking request = new StoreBooking(id, idUtente, idProiezione, numeroPosti);
        return tcpClient.sendRequest(request, StoreBookingResponse.class);
    }

    /**
     * Inserisce una nuova prenotazione nel sistema.
     *
     * @param idUtente     Identificativo dell'utente di tipo cliente che effettua la prenotazione.
     * @param idProiezione Identificativo della proiezione da prenotare.
     * @param numeroPosti  Numero di posti da assegnare.
     * @return Oggetto {@link StoreBookingResponse} con l'esito della creazione e i dati associati.
     */
    public StoreBookingResponse insertBooking(Integer idUtente, Integer idProiezione, Integer numeroPosti) {
        
        StoreBooking request = new StoreBooking(idUtente, idProiezione, numeroPosti);
        return tcpClient.sendRequest(request, StoreBookingResponse.class);
    }

    /**
     * Richiede la rimozione di una prenotazione.
     *
     * @param idPrenotazione Codice identificativodella prenotazione da cancellare.
     * @return Oggetto {@link DeleteBookingResponse} con l'esito dell'eliminazione.
     */
    public DeleteBookingResponse deleteBooking(Integer idPrenotazione) {
        
        DeleteBooking request = new DeleteBooking(idPrenotazione);
        return tcpClient.sendRequest(request, DeleteBookingResponse.class);
    }
}
