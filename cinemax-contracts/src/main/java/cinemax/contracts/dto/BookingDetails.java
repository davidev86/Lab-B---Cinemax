package cinemax.contracts.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

	public BookingDetails() {
	}

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

	public void setIdPrenotazione(Integer idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}

	public Integer getIdProiezione() {
		return idProiezione;
	}

	public void setIdProiezione(Integer idProiezione) {
		this.idProiezione = idProiezione;
	}

	public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCognomeCliente() {
		return cognomeCliente;
	}

	public void setCognomeCliente(String cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}

	public String getTitoloFilm() {
		return titoloFilm;
	}

	public void setTitoloFilm(String titoloFilm) {
		this.titoloFilm = titoloFilm;
	}

	public LocalDateTime getDataOraProiezione() {
		return dataOraProiezione;
	}

	public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
		this.dataOraProiezione = dataOraProiezione;
	}

	public Integer getNumeroPosti() {
		return numeroPosti;
	}

	public void setNumeroPosti(Integer numeroPosti) {
		this.numeroPosti = numeroPosti;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public BigDecimal getTotale() {
		return totale;
	}

	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
}