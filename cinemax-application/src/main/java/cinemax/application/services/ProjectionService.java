package cinemax.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cinemax.contracts.commands.StoreProjection;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmAndDate;
import cinemax.contracts.responses.ui.*;
import cinemax.contracts.responses.StoreProjectionResponse;

public class ProjectionService {

	private final TcpClient tcpClient;
	private Integer maxAvailableSeats = 200;

	public ProjectionService(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	public GetProjectionResponse getProjections(
			String titolo, 
			String genere, 
			LocalDate daDataProiezione, 
			LocalDate aDataProiezione, 
			BigDecimal daCosto, 
			BigDecimal aCosto) {

		GetProjections request = new GetProjections(titolo, genere, daDataProiezione, aDataProiezione, daCosto, aCosto);
		
		var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionResponse.class);
			
		return new GetProjectionResponse(Map(res.getProjections())); 
	}

	public GetProjectionResponse getProjectionsByFilmAndDate(String titoloFilm, LocalDate maxDataPrenotazione) {

		GetProjectionsByFilmAndDate request = new GetProjectionsByFilmAndDate(titoloFilm, maxDataPrenotazione);
		
		var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionResponse.class);
		return new GetProjectionResponse(Map(res.getProjections()));
	}

	public GetProjectionResponse getProjectionsByFilmTitle(String titoloFilm) {

		GetProjectionsByFilmAndDate request = new GetProjectionsByFilmAndDate(titoloFilm, (LocalDate.now()).plusDays(90));
		var res = tcpClient.sendRequest(request, cinemax.contracts.responses.GetProjectionResponse.class);
		return new GetProjectionResponse(Map(res.getProjections()));
	} 

	public StoreProjectionResponse insertProjection(Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {

		StoreProjection request = new StoreProjection(dataOraProiezione, idFilm, prezzoBiglietto);
		return tcpClient.sendRequest(request, StoreProjectionResponse.class);
	}

	public StoreProjectionResponse updateProjection(Integer id, Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {

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
}