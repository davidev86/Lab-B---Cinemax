package cinemax.contracts.requests;
import java.time.LocalDate;

import cinemax.contracts.interfaces.*;

public class GetProjections implements Query, ProjectionRequest  {

	private String titolo;
	private String genere;
	private LocalDate daDataPrenotazione;
	private LocalDate aDataPrenotazione;
	private Double daCosto;
	private Double aCosto;
	/**
	 * @return the titolo
	 */
	public String getTitolo() {
		return titolo;
	}
	/**
	 * @param titolo the titolo to set
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	/**
	 * @return the genere
	 */
	public String getGenere() {
		return genere;
	}
	/**
	 * @param genere the genere to set
	 */
	public void setGenere(String genere) {
		this.genere = genere;
	}
	/**
	 * @return the daDataPrenotazione
	 */
	public LocalDate getDaDataPrenotazione() {
		return daDataPrenotazione;
	}
	/**
	 * @param daDataPrenotazione the daDataPrenotazione to set
	 */
	public void setDaDataPrenotazione(LocalDate daDataPrenotazione) {
		this.daDataPrenotazione = daDataPrenotazione;
	}
	/**
	 * @return the aDataPrenotazione
	 */
	public LocalDate getaDataPrenotazione() {
		return aDataPrenotazione;
	}
	/**
	 * @param aDataPrenotazione the aDataPrenotazione to set
	 */
	public void setaDataPrenotazione(LocalDate aDataPrenotazione) {
		this.aDataPrenotazione = aDataPrenotazione;
	}

	public Double getDaCosto() {
		return daCosto;
	}
	public void setDaCosto(Double daCosto) {
		this.daCosto = daCosto;
	}
	public Double getaCosto() {
		return aCosto;
	}
	public void setaCosto(Double aCosto) {
		this.aCosto = aCosto;
	}
	
	
}
 