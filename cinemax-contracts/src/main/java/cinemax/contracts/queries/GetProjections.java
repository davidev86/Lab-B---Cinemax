/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;
import java.math.BigDecimal;
import java.time.LocalDate;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query parametrizzata per cercare proiezioni con filtri avanzati su titolo, genere, intervallo date proiezione e intervallo prezzi.
 */
public class GetProjections implements Query, ProjectionRequest  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String titolo;
	private String genere;
	private LocalDate daDataProiezione;
	private LocalDate aDataProiezione;
	private BigDecimal daCosto;
	private BigDecimal aCosto;
	
	public GetProjections() {
		
	}
	
	
	
	/**
	 * @param titolo
	 * @param genere
	 * @param daDataProiezione
	 * @param aDataProiezione
	 * @param daCosto
	 * @param aCosto
	 */
	public GetProjections(String titolo, String genere, LocalDate daDataProiezione, LocalDate aDataProiezione,
			BigDecimal daCosto, BigDecimal aCosto) {
		super();
		this.titolo = titolo; 
		this.genere = genere;
		this.daDataProiezione = daDataProiezione;
		this.aDataProiezione = aDataProiezione;
		this.daCosto = daCosto;
		this.aCosto = aCosto;
	}


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
 


