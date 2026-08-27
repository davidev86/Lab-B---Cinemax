/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.responses;

import java.util.List;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.interfaces.Response;

/**
 * Risposta inviata dal server contenente l'elenco dei film trovati.
 * <p>
 * Incapsula una lista di oggetti {@link FilmDetails} restituita a seguito dell'esecuzione
 * di una query di ricerca, filtraggio o consultazione del catalogo film.
 * </p>
 */
public class GetFilmsResponse implements Response {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Elenco contenente i dettagli dei film risultanti dalla query.
	 */
	private List<FilmDetails> films;

	/**
	 * Costruttore predefinito senza argomenti.
	 */
	public GetFilmsResponse() {
	}

	/**
	 * Costruisce una risposta contenente la lista specificata di film.
	 *
	 * @param films la lista di istanze {@link FilmDetails} da incapsulare nella risposta
	 */
	public GetFilmsResponse(List<FilmDetails> films) {
		this.films = films;
	}

	/**
	 * Restituisce la lista dei dettagli dei film.
	 *
	 * @return lista di {@link FilmDetails}
	 */
	public List<FilmDetails> getFilms() {
		return films;
	}

	/**
	 * Imposta la lista dei dettagli dei film.
	 *
	 * @param films lista di {@link FilmDetails} da assegnare alla risposta
	 */
	public void setFilms(List<FilmDetails> films) {
		this.films = films;
	}
}