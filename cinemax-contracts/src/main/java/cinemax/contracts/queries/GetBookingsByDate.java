/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
 */
package cinemax.contracts.queries;

import java.time.LocalDate;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per il recupero di tutte le prenotazioni relative a una specifica data di proiezione.
 * <p>
 * Incapsula la data di riferimento inviata dal client al server per filtrare
 * le prenotazioni pianificate nel giorno indicato.
 * </p>
 */
public class GetBookingsByDate implements Query, BookingRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Data di riferimento per il recupero e il filtraggio delle prenotazioni.
	 */
	private LocalDate date;

	/**
	 * Costruttore predefinito senza argomenti.
	 * <p>
	 * Necessario per framework di serializzazione, deserializzazione o binding via socket TCP.
	 * </p>
	 */
	public GetBookingsByDate() {
	}

	/**
	 * Crea un'istanza della query impostando la data specifica da ricercare.
	 *
	 * @param date la data per cui recuperare le prenotazioni
	 */
	public GetBookingsByDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Restituisce la data di riferimento impostata per la ricerca.
	 *
	 * @return la data della query come {@link LocalDate}, oppure {@code null} se non specificata
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Imposta la data di riferimento per la ricerca delle prenotazioni.
	 *
	 * @param date la nuova data di riferimento
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
} 