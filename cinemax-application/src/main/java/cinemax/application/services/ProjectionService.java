/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */

package cinemax.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cinemax.contracts.commands.DeleteProjection;
import cinemax.contracts.commands.StoreProjection;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.queries.GetProjectionById;
import cinemax.contracts.queries.GetProjectionHistory;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmAndDate;
import cinemax.contracts.queries.GetProjectionsByRangeDate;
import cinemax.contracts.responses.DeleteProjectionResponse;
import cinemax.contracts.responses.GetFilmResponse;
import cinemax.contracts.responses.StoreProjectionResponse;
import cinemax.contracts.responses.ui.GetProjectionResponse;
import cinemax.contracts.responses.ui.GetProjectionsResponse;

/**
 * Servizio applicativo per la gestione del palinsesto e delle proiezioni cinematografiche.
 * <p>
 * Gestisce l'interrogazione del catalogo proiezioni, il calcolo della disponibilità 
 * dei posti in sala, la validazione delle sovrapposizioni temporali di palinsesto 
 * e la persistenza tramite client TCP.
 */
public class ProjectionService {

    /** Client TCP per la trasmissione delle richieste al server. */
    private final TcpClient tcpClient;

    /** Capienza massima predefinita per ciascuna sala/proiezione. */
    private Integer maxAvailableSeats = 200;

    /**
     * Costruisce il servizio inizializzando il client di comunicazione.
     *
     * @param tcpClient Il client TCP configurato per l'inoltro delle richieste.
     */
    public ProjectionService(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    /**
     * Ricerca le proiezioni applicando filtri su titolo, genere, date e costi del biglietto.
     *
     * @param titolo           Titolo del film o sua sottostringa.
     * @param genere           Genere cinematografico.
     * @param daDataProiezione Data di inizio intervallo di programmazione.
     * @param aDataProiezione  Data di fine intervallo di programmazione.
     * @param daCosto          Costo minimo del biglietto.
     * @param aCosto           Costo massimo del biglietto.
     * @return Oggetto {@link GetProjectionsResponse} contenente i dettagli formattati per la UI.
     */
    public GetProjectionsResponse getProjections(
            String titolo, 
            String genere, 
            LocalDate daDataProiezione, 
            LocalDate aDataProiezione, 
            BigDecimal daCosto,  
            BigDecimal aCosto) {

        GetProjections request = new GetProjections(titolo, genere, daDataProiezione, aDataProiezione, daCosto, aCosto);
        var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
            
        return new GetProjectionsResponse(Map(res.getProjections())); 
    }

    /**
     * Ricerca le proiezioni attive per un film specifico fino a una data massima prefissata.
     *
     * @param titoloFilm          Titolo del film da cercare.
     * @param maxDataPrenotazione Limite temporale superiore entro cui cercare le proiezioni.
     * @return Oggetto {@link GetProjectionsResponse} con l'elenco delle proiezioni per la UI.
     */
    public GetProjectionsResponse getProjectionsByFilmAndDate(String titoloFilm, LocalDate maxDataPrenotazione) {
        GetProjectionsByFilmAndDate request = new GetProjectionsByFilmAndDate(titoloFilm, maxDataPrenotazione);
        var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
        return new GetProjectionsResponse(Map(res.getProjections()));
    }

    /**
     * Recupera le proiezioni programmate per un dato film nell'arco dei successivi 90 giorni.
     *
     * @param titoloFilm Titolo del film.
     * @return Oggetto {@link GetProjectionsResponse} con i risultati arricchiti con la disponibilità posti.
     */
    public GetProjectionsResponse getProjectionsByFilmTitle(String titoloFilm) {
        GetProjectionsByFilmAndDate request = new GetProjectionsByFilmAndDate(titoloFilm, LocalDate.now().plusDays(90));
        var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
        return new GetProjectionsResponse(Map(res.getProjections()));
    } 

    /**
     * Recupera i dettagli completi di una singola proiezione dato il suo identificativo.
     *
     * @param idProiezione Identificativo univoco della proiezione.
     * @return Oggetto {@link GetProjectionResponse} contenente la vista con il computo dei posti liberi.
     */
    public GetProjectionResponse getProjectionById(Integer idProiezione) {
        GetProjectionById request = new GetProjectionById(idProiezione);
        var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionResponse.class);
        return new GetProjectionResponse(toView(res.getProjection(), maxAvailableSeats));
    } 
    
    /**
     * Recupera le proiezioni comprese all'interno di un intervallo di data e ora.
     *
     * @param daDataProiezione Timestamp di inizio intervallo.
     * @param aDataProiezione  Timestamp di fine intervallo.
     * @return Oggetto {@link GetProjectionsResponse} con le proiezioni mappate per la UI.
     */
    public GetProjectionsResponse getProjectionsByDateRange(LocalDateTime daDataProiezione, LocalDateTime aDataProiezione) { 
        GetProjectionsByRangeDate request = new GetProjectionsByRangeDate(daDataProiezione, aDataProiezione);
        var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
        return new GetProjectionsResponse(Map(res.getProjections())); 
    }
    
    /**
     * Recupera l'intero storico delle proiezioni passate archiviate nel sistema.
     *
     * @return Oggetto {@link cinemax.contracts.responses.GetProjectionsResponse} con i dati storici.
     */
    public cinemax.contracts.responses.GetProjectionsResponse getHistoricalProjection() { 
        GetProjectionHistory request = new GetProjectionHistory();
        return tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
    }
    
    /**
     * Inserisce una nuova proiezione in palinsesto dopo aver verificato l'assenza di sovrapposizioni orarie.
     *
     * @param idFilm            Identificativo del film da proiettare.
     * @param dataOraProiezione Orario e data di inizio spettacolo.
     * @param prezzoBiglietto   Prezzo unitario del biglietto.
     * @return Oggetto {@link StoreProjectionResponse} con l'esito del salvataggio.
     * @throws IllegalArgumentException Se l'orario scelto si sovrappone a una proiezione già esistente.
     */
    public StoreProjectionResponse insertProjection(Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {        
        if (isProjectionOverlap(idFilm, dataOraProiezione, null)) {
            throw new IllegalArgumentException("Orario non disponibile: si sovrappone a un'altra proiezione in palinsesto.");
        }
        
        StoreProjection request = new StoreProjection(dataOraProiezione, idFilm, prezzoBiglietto);
        return tcpClient.sendRequest(request, StoreProjectionResponse.class);
    }
    
    /**
     * Rimuove una proiezione dal palinsesto.
     *
     * @param idProiezione Identificativo della proiezione da eliminare.
     * @return Oggetto {@link DeleteProjectionResponse} con l'esito dell'eliminazione.
     */
    public DeleteProjectionResponse deleteProjection(Integer idProiezione) {
        DeleteProjection request = new DeleteProjection(idProiezione);
        return tcpClient.sendRequest(request, DeleteProjectionResponse.class);
    }

    /**
     * Aggiorna le informazioni di una proiezione esistente, previa validazione delle sovrapposizioni orarie.
     *
     * @param id                Identificativo univoco della proiezione da modificare.
     * @param idFilm            Identificativo del film associato.
     * @param dataOraProiezione Nuovo orario di inizio spettacolo.
     * @param prezzoBiglietto   Nuovo costo del biglietto.
     * @return Oggetto {@link StoreProjectionResponse} con l'esito dell'aggiornamento.
     * @throws IllegalArgumentException Se il nuovo orario va in conflitto con altre proiezioni.
     */
    public StoreProjectionResponse updateProjection(Integer id, Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {
        if (isProjectionOverlap(idFilm, dataOraProiezione, id)) {
            throw new IllegalArgumentException("Orario non disponibile: si sovrappone a un'altra proiezione in palinsesto.");
        }
        
        StoreProjection request = new StoreProjection(id, dataOraProiezione, idFilm, prezzoBiglietto);
        return tcpClient.sendRequest(request, StoreProjectionResponse.class);    
    }


    /**
     * Converte una lista di DTO di dominio {@link ProjectionDetails} nei rispettivi modelli per la vista UI.
     *
     * @param projections Lista di record di proiezioni grezzi restituiti dal server.
     * @return Lista di oggetti {@link ProjectionDetailsView} pronti per la visualizzazione.
     */
    public List<ProjectionDetailsView> Map(List<ProjectionDetails> projections) {
        List<ProjectionDetailsView> res = new ArrayList<ProjectionDetailsView>();
        for (ProjectionDetails projectionDetails : projections) {
            res.add(toView(projectionDetails, maxAvailableSeats));
        }
        return res;
    }

    /**
     * Mappa un singolo DTO di proiezione in un oggetto view calcolando i posti rimanenti.
     *
     * @param source            L'oggetto dati sorgente {@link ProjectionDetails}.
     * @param maxAvailableSeats La capienza massima della sala.
     * @return Oggetto {@link ProjectionDetailsView} compilato, oppure {@code null} se la sorgente è nulla.
     */
    private ProjectionDetailsView toView(ProjectionDetails source, Integer maxAvailableSeats) {
        if (source == null) {
            return null;
        }

        ProjectionDetailsView view = new ProjectionDetailsView();
        view.setId(source.getId());
        view.setIdFilm(source.getIdFilm());
        view.setDataOraProiezione(source.getDataOraProiezione());
        view.setTitoloFilm(source.getTitoloFilm());
        view.setGenere(source.getGenere());
        view.setRegista(source.getRegista());
        view.setAnno(source.getAnno());
        view.setDurataMinuti(source.getDurataMinuti());
        view.setEtaMinima(source.getEtaMinima());
        view.setCosto(source.getCosto());
        view.setTotalePostiPrenotati(source.getTotalePostiPrenotati());

        // Calcolo e assegnazione dei posti liberi (valore minimo: 0)
        if (source.getTotalePostiPrenotati() != null) {
            Integer liberi = GetAvailableSeats(source);
            view.setTotalePostiLiberi(Math.max(0, liberi));
        } else {
            view.setTotalePostiLiberi(null);
        }

        return view;
    } 

    /**
     * Calcola i posti ancora disponibili per la proiezione sottraendo le prenotazioni dalla capienza totale.
     *
     * @param projection Dati della proiezione contenenti il conteggio dei posti già riservati.
     * @return Numero di posti rimasti liberi.
     */
    private Integer GetAvailableSeats(ProjectionDetails projection) {
        return maxAvailableSeats - projection.getTotalePostiPrenotati();
    }    
    
    /**
     * Controlla se l'orario richiesto per un film collide con altre proiezioni in palinsesto.
     * <p>
     * Include un margine di sicurezza di 30 minuti prima dell'inizio e dopo la fine 
     * della pellicola (in base alla sua durata effettiva) per le operazioni di sala.
     *
     * @param idFilm            Identificativo del film per ottenerne la durata in minuti.
     * @param dataOraProiezione Orario di avvio della proiezione proposta.
     * @return {@code true} se esiste un conflitto di programmazione nell'intervallo, {@code false} altrimenti.
     */
    private Boolean isProjectionOverlap(Integer idFilm, LocalDateTime dataOraProiezione, Integer idProiezioneDaEscludere) {
        FilmService filmService = new FilmService(tcpClient);
        GetFilmResponse response = filmService.getFilmById(idFilm);

        Integer durataNuovaProiezione = response.getFilm().getDurataMinuti();

        LocalDateTime nuovaInizio = dataOraProiezione;
        LocalDateTime nuovaFineConPulizia = dataOraProiezione.plusMinutes(durataNuovaProiezione + 30);

        // Le proiezioni vengono pianificate nella stessa giornata (niente fascia notturna)
        LocalDateTime ricercaDa = dataOraProiezione.toLocalDate().atStartOfDay();
        LocalDateTime ricercaA = dataOraProiezione.toLocalDate().atTime(23, 59, 59);

        GetProjectionsResponse res = getProjectionsByDateRange(ricercaDa, ricercaA);
        if (res == null || res.getProjections() == null) {
            return false;
        }

        for (ProjectionDetailsView projection : res.getProjections()) {
            if (idProiezioneDaEscludere != null && idProiezioneDaEscludere.equals(projection.getId())) {
                continue;
            }
            if (projection.getDataOraProiezione() == null || projection.getDurataMinuti() == null) {
                continue;
            }

            LocalDateTime esistenteInizio = projection.getDataOraProiezione();
            LocalDateTime esistenteFineConPulizia = esistenteInizio.plusMinutes(projection.getDurataMinuti() + 30);

            boolean nonSiSovrappongono = !esistenteFineConPulizia.isAfter(nuovaInizio)
                    || !nuovaFineConPulizia.isAfter(esistenteInizio);

            if (!nonSiSovrappongono) {
                return true;
            }
        }

        return false;
    }
}