package cinemax.contracts.queries;
import java.math.BigDecimal;
import java.time.LocalDate;

import cinemax.contracts.interfaces.*;

public class GetProjections implements Query, ProjectionRequest  {

	private String titolo;
	private String genere;
	private LocalDate daDataProiezione;
	private LocalDate aDataProiezione;
	private BigDecimal daCosto;
	private BigDecimal aCosto;
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
	

	public BigDecimal getDaCosto() {
		return daCosto;
	}
	public void setDaCosto(BigDecimal daCosto) {
		this.daCosto = daCosto;
	}
	public BigDecimal getaCosto() {
		return aCosto;
	}
	public void setaCosto(BigDecimal aCosto) {
		this.aCosto = aCosto;
	}
	public LocalDate getaDataProiezione() {
		return aDataProiezione;
	}
	public void setaDataProiezione(LocalDate aDataProiezione) {
		this.aDataProiezione = aDataProiezione;
	}
	public LocalDate getDaDataProiezione() {
		return daDataProiezione;
	}
	public void setDaDataProiezione(LocalDate daDataProiezione) {
		this.daDataProiezione = daDataProiezione;
	}
	
	
}
 