/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO che rappresenta i dettagli di una prenotazione (booking) restituiti dal server.
 * Include informazioni su cliente, proiezione, numero di posti e prezzi.
 */
public class BookingDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idPrenotazione;
	private Integer idProiezione;
	private Integer idUtente;
	private String nomeCliente;
	private String cognomeCliente;
	private String titoloFilm;
	private LocalDateTime dataOraProiezione;
	private Integer numeroPosti;
	private BigDecimal costo;
	private BigDecimal totale;

	/**
	 * Costruttore vuoto per consentire la serializzazione dell'oggetto via TCP.
	 */
	public BookingDetails() {
	}

	/**
	 * Costruisce un oggetto BookingDetails con tutte le informazioni principali
	 * relative a una prenotazione.
	 *
	 * @param idPrenotazione identificativo della prenotazione (null se non ancora assegnato)
	 * @param idProiezione identificativo della proiezione associata
	 * @param idUtente identificativo dell'utente che ha effettuato la prenotazione
	 * @param nomeCliente nome del cliente associato alla prenotazione
	 * @param cognomeCliente cognome del cliente
	 * @param titoloFilm titolo del film
	 * @param dataOraProiezione data e ora della proiezione
	 * @param numeroPosti numero di posti prenotati
	 * @param costo costo unitario del biglietto per la proiezione
	 * @param totale importo totale della prenotazione (costo * numeroPosti, se applicabile)
	 */
	public BookingDetails(Integer idPrenotazione, Integer idProiezione, Integer idUtente, String nomeCliente,
			String cognomeCliente, String titoloFilm, LocalDateTime dataOraProiezione, Integer numeroPosti,
			BigDecimal costo, BigDecimal totale) {
		this.idPrenotazione = idPrenotazione;
		this.idProiezione = idProiezione;
		this.idUtente = idUtente;
		this.nomeCliente = nomeCliente;
		this.cognomeCliente = cognomeCliente;
		this.titoloFilm = titoloFilm;
		this.dataOraProiezione = dataOraProiezione;
		this.numeroPosti = numeroPosti;
		this.costo = costo;
		this.totale = totale;
	}

	/**
	 * Identificatore della prenotazione.
	 * @return id della prenotazione oppure null se non disponibile
	 */
	
	public Integer getIdPrenotazione() {
		return idPrenotazione;
	}
	/**
	 * Identificatore della prenotazione
	 * @param idPrenotazione il nuovo identificatore univoco della prenotazione.
	 */
	public void setIdPrenotazione(Integer idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}

	/**
	 * Identificatore della proiezione associata.
	 * @return id della proiezione
	 */
	
	public Integer getIdProiezione() {
		return idProiezione;
	}
	/**
	 * Identificatore della proiezione associata.
	 * @param id della proiezione
	 */
	public void setIdProiezione(Integer idProiezione) {
		this.idProiezione = idProiezione;
	}

	/**
	 * Identificatore dell'utente che ha effettuato la prenotazione.
	 * @return id utente
	 */
	
	public Integer getIdUtente() {
		return idUtente;
	}
	/**
	 * Identificatore dell'utente che ha effettuato la prenotazione.
	 * @param id utente
	 */
	
	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	/**
	 * Nome del cliente associato alla prenotazione.
	 * @return nome del cliente o null
	 */
	
	public String getNomeCliente() {
		return nomeCliente;
	}
	/**
	 * Nome del cliente associato alla prenotazione.
	 * @param nome del cliente
	 */
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	/**
	 * Cognome del cliente associato alla prenotazione.
	 * @return cognome del cliente o null
	 */
	
	public String getCognomeCliente() {
		return cognomeCliente;
	}
	/**
	 * Cognome del cliente associato alla prenotazione.
	 * @param cognome del cliente
	 */
	public void setCognomeCliente(String cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}

	/**
	 * Titolo del film prenotato.
	 * @return titolo del film o null
	 */
	
	public String getTitoloFilm() {
		return titoloFilm;
	}
	/**
	 * Titolo del film prenotato.
	 * @param titolo del film
	 */
	public void setTitoloFilm(String titoloFilm) {
		this.titoloFilm = titoloFilm;
	}
	/**
	 * Data e ora della proiezione.
	 * @return data e ora come {@link LocalDateTime} o null
	 */
	
	public LocalDateTime getDataOraProiezione() {
		return dataOraProiezione;
	}
	/**
	 * Data e ora della proiezione.
	 * @param data e ora come {@link LocalDateTime}
	 */
	public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
		this.dataOraProiezione = dataOraProiezione;
	}
	/**
	 * Numero di posti prenotati.
	 * @return numero di posti (null se non specificato)
	 */
	
	public Integer getNumeroPosti() {
		return numeroPosti;
	}
	/**
	 * Numero di posti prenotati.
	 * @param numero di posti
	 */
	public void setNumeroPosti(Integer numeroPosti) {
		this.numeroPosti = numeroPosti;
	}

	/**
	 * Costo unitario del biglietto per la proiezione.
	 * @return costo come {@link BigDecimal}
	 */
	public BigDecimal getCosto() {
		return costo;
	}
	/**
	 * Costo unitario del biglietto per la proiezione.
	 * @param costo come {@link BigDecimal}
	 */
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	/**
	 * Importo totale della prenotazione (costo * numeroPosti).
	 * @return totale come {@link BigDecimal}
	 */	
	public BigDecimal getTotale() {
		return totale;
	}
	/**
	 * Importo totale della prenotazione (costo * numeroPosti).
	 * @param totale come {@link BigDecimal}
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	
	/**
	 * Restituisce una rappresentazione testuale riassuntiva della prenotazione.
	 * <p>
	 * La stringa generata include la data e l'ora della proiezione formattate ({@code dd/MM/yyyy HH:mm}), 
	 * il titolo del film, il numero di posti e il totale monetario con due cifre decimali 
	 * (es. {@code "26/08/2026 21:00 | Inception | 2 posti | €15.00"}).
	 * In caso di valori nulli, vengono applicati testi e valori di default (es. "Data non disponibile", "N/D", 0).
	 * </p>
	 *
	 * @return una stringa formattata contenente i dettagli principali della prenotazione
	 */
	
	@Override
	public String toString() {
	    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    String dataFormattata = (dataOraProiezione != null) ? dataOraProiezione.format(formatter) : "Data non disponibile";
	    BigDecimal totaleVal = (totale != null) ? totale : BigDecimal.ZERO;
	    int posti = (numeroPosti != null) ? numeroPosti : 0;

	    return String.format("%s | %s | %d posti | €%.2f", 
	        dataFormattata, 
	        titoloFilm != null ? titoloFilm : "N/D",	
	        posti, 
	        totaleVal);
	}
	

}

