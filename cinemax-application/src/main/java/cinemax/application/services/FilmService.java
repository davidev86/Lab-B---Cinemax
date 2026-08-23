package cinemax.application.services;

import cinemax.contracts.queries.GetFilmsById;
import cinemax.contracts.queries.GetFilmsByTitle;
import cinemax.contracts.responses.GetFilmResponse;
import cinemax.contracts.responses.GetFilmsResponse;

public class FilmService {

	private final TcpClient tcpClient;

	// Il costruttore richiede il client
	public FilmService(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	} 
 
	public GetFilmsResponse getFilmsByTitle(String titoloFilm) {
		 
		GetFilmsByTitle request = new GetFilmsByTitle(titoloFilm);
		return tcpClient.sendRequest(request, GetFilmsResponse.class);
	}

	public GetFilmResponse getFilmById(Integer id) {
		GetFilmsById request = new GetFilmsById(id);
		return tcpClient.sendRequest(request, GetFilmResponse.class);
	}
}