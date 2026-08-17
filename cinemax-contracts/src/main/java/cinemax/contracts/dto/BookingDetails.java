package cinemax.contracts.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nomeCliente;
	private String cognomeCliente;
	private String titoloFilm;
	private LocalDateTime dataOraProiezione;
	private Integer numeroPosti;
	private BigDecimal costo;
	private BigDecimal totale;

	public BookingDetails() {
	}

	public BookingDetails(Integer id, Integer idUtente, String nomeCliente, String cognomeCliente, String titoloFilm,
			LocalDateTime dataOraProiezione, Integer numeroPosti, BigDecimal costo, BigDecimal totale) {
		this.id = id;
		this.nomeCliente = nomeCliente;
		this.cognomeCliente = cognomeCliente;
		this.titoloFilm = titoloFilm;
		this.dataOraProiezione = dataOraProiezione;
		this.numeroPosti = numeroPosti;
		this.costo = costo;
		this.totale = totale;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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