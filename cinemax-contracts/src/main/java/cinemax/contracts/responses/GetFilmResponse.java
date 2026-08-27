/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server contenente i dettagli completi di un singolo film.
 * <p>
 * Incapsula un'istanza di {@link FilmDetails} restituita a seguito dell'elaborazione
 * di una richiesta di ricerca film per identificativo.
 * </p>
 */
public class GetFilmResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Dettagli descrittivi del film incapsulati nella risposta.
	 */
	private FilmDetails projection;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetFilmResponse() {
	}

	/**
	 * Costruisce una risposta specificando i dettagli del film.
	 *
	 * @param projection l'istanza di {@link FilmDetails} contenente le informazioni del film
	 */
	public GetFilmResponse(FilmDetails projection) {
		this.projection = projection;
	}

	/**
	 * Restituisce i dettagli del film incapsulati nella risposta.
	 *
	 * @return l'oggetto {@link FilmDetails}
	 */
	public FilmDetails getFilm() {
		return projection;
	}

	/**
	 * Imposta i dettagli del film da restituire al client.
	 *
	 * @param projection
	 */
	public void setFilm(FilmDetails projection) {
		this.projection = projection;
	}
}