package cinemax.contracts.queries;

import java.time.LocalDate;
import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

public class GetBookings implements Query, ProjectionRequest {

	private static final long serialVersionUID = 1L;

	private Integer codicePrenotazione;
	private String nomeCliente;
	private String cognomeCliente;
	private String titoloFilm;
	private LocalDate daDataProiezione;
	private LocalDate aDataProiezione;

	public GetBookings() {
	}

	public GetBookings(Integer codicePrenotazione, String nomeCliente, String cognomeCliente, 
	                   String titoloFilm, LocalDate daDataProiezione, LocalDate aDataProiezione) {
		this.codicePrenotazione = codicePrenotazione;
		this.nomeCliente = nomeCliente;
		this.cognomeCliente = cognomeCliente;
		this.titoloFilm = titoloFilm;
		this.daDataProiezione = daDataProiezione;
		this.aDataProiezione = aDataProiezione;
	}

	public Integer getCodicePrenotazione() {
		return codicePrenotazione;
	}

	public void setCodicePrenotazione(Integer codicePrenotazione) {
		this.codicePrenotazione = codicePrenotazione;
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

	public LocalDate getDaDataProiezione() {
		return daDataProiezione;
	}

	public void setDaDataProiezione(LocalDate daDataProiezione) {
		this.daDataProiezione = daDataProiezione;
	}

	public LocalDate getADataProiezione() {
		return aDataProiezione;
	}

	public void setADataProiezione(LocalDate aDataProiezione) {
		this.aDataProiezione = aDataProiezione;
	}
}