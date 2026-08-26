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

	public Integer getIdPrenotazione() {
		return idPrenotazione;
	}
	/**
	 * Identificatore della prenotazione.
	 * @return id della prenotazione oppure null se non disponibile
	 */
	public void setIdPrenotazione(Integer idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}

	public Integer getIdProiezione() {
		return idProiezione;
	}
	/**
	 * Identificatore della proiezione associata.
	 * @return id della proiezione
	 */
	public void setIdProiezione(Integer idProiezione) {
		this.idProiezione = idProiezione;
	}

	public Integer getIdUtente() {
		return idUtente;
	}
	/**
	 * Identificatore dell'utente che ha effettuato la prenotazione.
	 * @return id utente
	 */
	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}
	/**
	 * Nome del cliente associato alla prenotazione.
	 * @return nome del cliente o null
	 */
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCognomeCliente() {
		return cognomeCliente;
	}
	/**
	 * Cognome del cliente associato alla prenotazione.
	 * @return cognome del cliente o null
	 */
	public void setCognomeCliente(String cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}

	public String getTitoloFilm() {
		return titoloFilm;
	}
	/**
	 * Titolo del film prenotato.
	 * @return titolo del film o null
	 */
	public void setTitoloFilm(String titoloFilm) {
		this.titoloFilm = titoloFilm;
	}

	public LocalDateTime getDataOraProiezione() {
		return dataOraProiezione;
	}
	/**
	 * Data e ora della proiezione.
	 * @return data e ora come {@link LocalDateTime} o null
	 */
	public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
		this.dataOraProiezione = dataOraProiezione;
	}

	public Integer getNumeroPosti() {
		return numeroPosti;
	}
	/**
	 * Numero di posti prenotati.
	 * @return numero di posti (null se non specificato)
	 */
	public void setNumeroPosti(Integer numeroPosti) {
		this.numeroPosti = numeroPosti;
	}

	public BigDecimal getCosto() {
		return costo;
	}
	/**
	 * Costo unitario del biglietto per la proiezione.
	 * @return costo come {@link BigDecimal}
	 */
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public BigDecimal getTotale() {
		return totale;
	}
	/**
	 * Importo totale della prenotazione (costo * numeroPosti).
	 * @return totale come {@link BigDecimal}
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	
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

