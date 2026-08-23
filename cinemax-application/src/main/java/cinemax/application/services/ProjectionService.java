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
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmAndDate;
import cinemax.contracts.queries.GetProjectionsByRangeDate;
import cinemax.contracts.responses.DeleteProjectionResponse;
import cinemax.contracts.responses.GetFilmResponse;
import cinemax.contracts.responses.StoreProjectionResponse;
import cinemax.contracts.responses.ui.GetProjectionResponse;
import cinemax.contracts.responses.ui.GetProjectionsResponse;

public class ProjectionService {

	private final TcpClient tcpClient;
	private Integer maxAvailableSeats = 200;

	public ProjectionService(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

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

	public GetProjectionsResponse getProjectionsByFilmAndDate(String titoloFilm, LocalDate maxDataPrenotazione) {

		GetProjectionsByFilmAndDate request = new GetProjectionsByFilmAndDate(titoloFilm, maxDataPrenotazione);
		
		var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
		return new GetProjectionsResponse(Map(res.getProjections()));
	}

	public GetProjectionsResponse getProjectionsByFilmTitle(String titoloFilm) {

		GetProjectionsByFilmAndDate request = new GetProjectionsByFilmAndDate(titoloFilm, (LocalDate.now()).plusDays(90));
		var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
		return new GetProjectionsResponse(Map(res.getProjections()));
	} 

	public GetProjectionResponse getProjectionById(Integer idProiezione) {

		GetProjectionById request = new GetProjectionById(idProiezione);
		var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionResponse.class);
		return new GetProjectionResponse(toView(res.getProjection(), maxAvailableSeats));
	} 
	
	public GetProjectionsResponse getProjectionsByDateRange(			
			LocalDateTime daDataProiezione, 
			LocalDateTime aDataProiezione
			) { 

		GetProjectionsByRangeDate request = new GetProjectionsByRangeDate( daDataProiezione, aDataProiezione);
		
		var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
			
		return new GetProjectionsResponse(Map(res.getProjections())); 
	}
	
	public cinemax.contracts.responses.GetProjectionsResponse getHistoricalProjection(			
			LocalDateTime daDataProiezione, 
			LocalDateTime aDataProiezione
			) { 

		GetProjectionsByRangeDate request = new GetProjectionsByRangeDate( daDataProiezione, aDataProiezione);
		return tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionsResponse.class);
	}
	
	public StoreProjectionResponse insertProjection(Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {		

		if(IsTheProjectionOverlap(idFilm, dataOraProiezione))
			throw new IllegalArgumentException("Orario non disponibile: si sovrappone a un'altra proiezione in palinsesto.");
		
		StoreProjection request = new StoreProjection(dataOraProiezione, idFilm, prezzoBiglietto);
		return tcpClient.sendRequest(request, StoreProjectionResponse.class);
	}
	
	public DeleteProjectionResponse deleteProjection(Integer idProiezione) {
	    DeleteProjection request = new DeleteProjection(idProiezione);
	    return tcpClient.sendRequest(request, DeleteProjectionResponse.class);
	}

	public StoreProjectionResponse updateProjection(Integer id, Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {

		if(IsTheProjectionOverlap(idFilm, dataOraProiezione))
			throw new IllegalArgumentException("Orario non disponibile: si sovrappone a un'altra proiezione in palinsesto.");
		
		StoreProjection request = new StoreProjection(id, dataOraProiezione, idFilm, prezzoBiglietto);
		return tcpClient.sendRequest(request, StoreProjectionResponse.class);	
	}

	public List<ProjectionDetailsView> Map(List<ProjectionDetails> projections){
		List<ProjectionDetailsView> res = new ArrayList<ProjectionDetailsView>();
		for (ProjectionDetails projectionDetails : projections) 
			res.add(toView(projectionDetails, maxAvailableSeats));

		return res;
	}

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

		// Calcolo e assegnazione dei posti liberi
		if (source.getTotalePostiPrenotati() != null) {
			Integer liberi = GetAvailableSeats(source);
			view.setTotalePostiLiberi(Math.max(0, liberi));
		} else {
			view.setTotalePostiLiberi(null);
		}

		return view;
	} 

	private Integer GetAvailableSeats(ProjectionDetails projection) {
		return maxAvailableSeats - projection.getTotalePostiPrenotati();
	}	
	
	private Boolean IsTheProjectionOverlap(Integer idFilm, LocalDateTime dataOraProiezione) {
		//Get film 
		FilmService filmService = new FilmService(tcpClient);
		GetFilmResponse response = filmService.getFilmById(idFilm);
		 
		Integer durata = response.getFilm().getDurataMinuti(); 
		
		//Set date range
		LocalDateTime from = dataOraProiezione.minusMinutes(30);
		LocalDateTime to = dataOraProiezione.plusMinutes(durata + 30);
		
		GetProjectionsResponse res = getProjectionsByDateRange(from, to );
		
		return res != null && !res.getProjections().isEmpty();
	}
}