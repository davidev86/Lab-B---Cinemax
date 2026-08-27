/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server contenente l'elenco dei dettagli delle prenotazioni trovate.
 * <p>
 * Incapsula una collezione di oggetti {@link BookingDetails} restituita a seguito dell'esecuzione
 * di una query di ricerca o filtraggio prenotazioni.
 * </p>
 */
public class GetBookingResponse implements Response {

	private static final long serialVersionUID = 1L;

	/**
	 * Elenco contenente i dettagli delle prenotazioni risultanti dalla ricerca.
	 */
	private List<BookingDetails> bookings;

	/**
	 * Costruttore predefinito senza argomenti.
	 
	 */
	public GetBookingResponse() {
	}

	/**
	 * Costruisce una risposta contenente la lista specificata di prenotazioni.
	 *
	 * @param bookings la lista di istanze {@link BookingDetails} da incapsulare nella risposta
	 */
	public GetBookingResponse(List<BookingDetails> bookings) {
		this.bookings = bookings;
	}

	/**
	 * Restituisce la lista dei dettagli delle prenotazioni.
	 *
	 * @return la lista di {@link BookingDetails}, oppure {@code null} se non impostata
	 */
	public List<BookingDetails> getBookings() {
		return bookings;
	}

	/**
	 * Imposta la lista dei dettagli delle prenotazioni.
	 *
	 * @param bookings
	 */
	public void setBookings(List<BookingDetails> bookings) {
		this.bookings = bookings;
	}
}