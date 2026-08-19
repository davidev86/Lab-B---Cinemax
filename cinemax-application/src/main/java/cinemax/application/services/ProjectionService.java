package cinemax.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cinemax.contracts.commands.StoreProjection;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmIdAndDate;
import cinemax.contracts.responses.GetProjectionResponse;
import cinemax.contracts.responses.StoreProjectionResponse;

public class ProjectionService {

	private final TcpClient tcpClient;

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
		return tcpClient.sendRequest(request, GetProjectionResponse.class);
	}

	public GetProjectionResponse getProjectionsByFilmIdAndDate(String titoloFilm, LocalDate maxDataPrenotazione) {
		
		GetProjectionsByFilmIdAndDate request = new GetProjectionsByFilmIdAndDate(titoloFilm, maxDataPrenotazione);
		return tcpClient.sendRequest(request, GetProjectionResponse.class);
	} 

	public StoreProjectionResponse insertProjection(Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {
		
		StoreProjection request = new StoreProjection(dataOraProiezione, idFilm, prezzoBiglietto);
		return tcpClient.sendRequest(request, StoreProjectionResponse.class);
	}

	public StoreProjectionResponse updateProjection(Integer id, Integer idFilm, LocalDateTime dataOraProiezione, BigDecimal prezzoBiglietto) {
		
		StoreProjection request = new StoreProjection(id, dataOraProiezione, idFilm, prezzoBiglietto);
		return tcpClient.sendRequest(request, StoreProjectionResponse.class);
	}
}