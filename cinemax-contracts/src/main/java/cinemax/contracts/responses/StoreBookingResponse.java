package cinemax.contracts.responses;

import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server di conferma dell'avvenuta memorizzazione o registrazione di una prenotazione.
 * <p>
 * Incapsula l'esito dell'operazione e l'identificatore univoco assegnato alla prenotazione
 * (chiave primaria generata dal database) a seguito dell'elaborazione di una richiesta di inserimento.
 * </p>
 */
public class StoreBookingResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco della prenotazione generato o memorizzato nel database.
	 */
	private Integer id;

	/**
	 * Esito dell'operazione di memorizzazione ({@code true} se completata con successo, {@code false} altrimenti).
	 */
	private boolean success;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public StoreBookingResponse() {
	}

	/**
	 * Costruisce una risposta di successo specificando l'identificatore della prenotazione.
	 *
	 * @param id l'identificatore univoco assegnato alla prenotazione creata
	 */
	public StoreBookingResponse(Integer id) {
		this.id = id;
		this.success = true;
	}

	/**
	 * Costruisce una risposta specificando l'identificatore della prenotazione e l'esito esplicito dell'operazione.
	 *
	 * @param id      l'identificatore univoco assegnato alla prenotazione
	 * @param success l'esito dell'operazione di inserimento
	 */
	public StoreBookingResponse(Integer id, boolean success) {
		this.id = id;
		this.success = success;
	}

	/**
	 * Restituisce l'identificatore univoco della prenotazione memorizzata.
	 *
	 * @return l'identificatore numerico della prenotazione, oppure {@code null} se non presente
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore univoco della prenotazione memorizzata.
	 *
	 * @param id il nuovo identificatore numerico della prenotazione
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Indica se l'operazione di memorizzazione è andata a buon fine.
	 *
	 * @return {@code true} se la prenotazione è stata salvata con successo, {@code false} altrimenti
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Imposta l'esito dell'operazione di memorizzazione della prenotazione.
	 *
	 * @param success il flag di stato dell'operazione
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}