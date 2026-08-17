package cinemax.application.services;

import java.time.LocalDate;

import cinemax.contracts.commands.StoreBooking;
import cinemax.contracts.queries.GetBookings;
import cinemax.contracts.queries.GetBookingsByUserId;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.StoreBookingResponse;

public class BookingService {

	private final TcpClient tcpClient;

	// Il costruttore richiede il client
	public BookingService(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	} 
 
	public GetBookingResponse getBookings(Integer codicePrenotazione, String nomeCliente, String cognomeCliente, String titoloFilm, LocalDate daDataProiezione, LocalDate aDataProiezione) {
		
		GetBookings request = new GetBookings(codicePrenotazione, nomeCliente, cognomeCliente, titoloFilm, daDataProiezione, aDataProiezione);
		return tcpClient.sendRequest(request, GetBookingResponse.class);
	}

	public GetBookingResponse getBookingsByUserId(Integer idUtente) {
		
		GetBookingsByUserId request = new GetBookingsByUserId(idUtente);
		return tcpClient.sendRequest(request, GetBookingResponse.class);
	}

	public StoreBookingResponse updateBooking(Integer id, Integer idUtente, Integer idProiezione, Integer numeroPosti) {
		
		StoreBooking request = new StoreBooking(id, idUtente, idProiezione, numeroPosti);
		return tcpClient.sendRequest(request, StoreBookingResponse.class);
	}

	public StoreBookingResponse insertBooking(Integer idUtente, Integer idProiezione, Integer numeroPosti) {
		
		StoreBooking request = new StoreBooking(idUtente, idProiezione, numeroPosti);
		return tcpClient.sendRequest(request, StoreBookingResponse.class);
	}
}