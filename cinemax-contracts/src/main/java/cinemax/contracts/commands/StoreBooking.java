/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Command;

/**
 * Comando per la creazione o l'aggiornamento di una prenotazione nel sistema.
 * <p>
 * Incapsula i dati necessari (utente, proiezione, numero di posti ed eventuale identificativo)
 * per l'elaborazione e la persistenza della prenotazione lato server.
 * </p>
 */
public class StoreBooking implements Command, BookingRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco della prenotazione 
	 */
	private Integer id;

	/**
	 * Identificatore univoco dell'utente che effettua la prenotazione.
	 */
	private Integer idUtente;

	/**
	 * Identificatore univoco della proiezione cinematografica selezionata.
	 */
	private Integer idProiezione;

	/**
	 * Numero di posti richiesti per la prenotazione.
	 */
	private Integer numeroPosti;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public StoreBooking() {
	}

	/**
	 * Costruttore per la creazione di una nuova prenotazione (inserimento).
	 * <p>
	 * L'identificativo della prenotazione non viene fornito poiché verrà generato dal database.
	 * </p>
	 *
	 * @param idUtente      l'identificatore univoco dell'utente che prenota
	 * @param idProiezione l'identificatore univoco della proiezione scelta
	 * @param numeroPosti   il numero di posti da riservare
	 */
	public StoreBooking(Integer idUtente, Integer idProiezione, Integer numeroPosti) {
		this.idUtente = idUtente;
		this.idProiezione = idProiezione;
		this.numeroPosti = numeroPosti;
	}

	/**
	 * Costruttore per la modifica di una prenotazione esistente (aggiornamento).
	 *
	 * @param id            l'identificatore univoco della prenotazione da aggiornare
	 * @param idUtente      l'identificatore univoco dell'utente associato
	 * @param idProiezione l'identificatore univoco della proiezione associata
	 * @param numeroPosti   il nuovo numero di posti da riservare
	 */
	public StoreBooking(Integer id, Integer idUtente, Integer idProiezione, Integer numeroPosti) {
		this.id = id;
		this.idUtente = idUtente;
		this.idProiezione = idProiezione;
		this.numeroPosti = numeroPosti;
	}

	/**
	 * Restituisce l'identificatore univoco della prenotazione.
	 *
	 * @return l'identificatore numerico della prenotazione, oppure null se non disponibile
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore univoco della prenotazione.
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce l'identificatore dell'utente associato alla prenotazione.
	 *
	 * @return l'id dell'utente
	 */
	public Integer getIdUtente() {
		return idUtente;
	}

	/**
	 * Imposta l'identificatore dell'utente associato alla prenotazione.
	 *
	 * @param idUtente
	 */
	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	/**
	 * Restituisce l'identificatore della proiezione associata alla prenotazione.
	 *
	 * @return l'id della proiezione
	 */
	public Integer getIdProiezione() {
		return idProiezione;
	}

	/**
	 * Imposta l'identificatore della proiezione associata alla prenotazione.
	 *
	 * @param idProiezione
	 */
	public void setIdProiezione(Integer idProiezione) {
		this.idProiezione = idProiezione;
	}

	/**
	 * Restituisce il numero di posti riservati con la prenotazione.
	 *
	 * @return il numero di posti
	 */
	public Integer getNumeroPosti() {
		return numeroPosti;
	}

	/**
	 * Imposta il numero di posti da riservare con la prenotazione.
	 *
	 * @param numeroPosti
	 */
	public void setNumeroPosti(Integer numeroPosti) {
		this.numeroPosti = numeroPosti;
	}
}
