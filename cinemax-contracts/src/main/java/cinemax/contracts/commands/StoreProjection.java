/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.ProjectionRequest;

/**
 * Command per creare o aggiornare una proiezione (projection).
 * Contiene informazioni come data/ora, film associato e prezzo del biglietto.
 */
public class StoreProjection implements Command, ProjectionRequest{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private LocalDateTime DataOraProiezione;
	private Integer idFilm;
	private BigDecimal prezzoBiglietto;
	
	public StoreProjection() {}
	
	/**
	 * @param id
	 * @param dataOraProiezione
	 * @param idFilm
	 * @param prezzoBiglietto
	 */
	public StoreProjection(Integer id, LocalDateTime dataOraProiezione, Integer idFilm, BigDecimal prezzoBiglietto) {
		super();
		this.id = id;
		DataOraProiezione = dataOraProiezione;
		this.idFilm = idFilm;
		this.prezzoBiglietto = prezzoBiglietto;
	}
	
	public StoreProjection(LocalDateTime dataOraProiezione, Integer idFilm, BigDecimal prezzoBiglietto) {
		super();
		this.id = null;
		DataOraProiezione = dataOraProiezione;
		this.idFilm = idFilm;
		this.prezzoBiglietto = prezzoBiglietto;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDateTime getDataOraProiezione() {
		return DataOraProiezione;
	}
	public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
		DataOraProiezione = dataOraProiezione;
	}
	public Integer getIdFilm() {
		return idFilm;
	}
	public void setIdFilm(Integer idFilm) {
		this.idFilm = idFilm;
	}
	public BigDecimal getPrezzoBiglietto() {
		return prezzoBiglietto;
	}
	public void setPrezzoBiglietto(BigDecimal prezzoBiglietto) {
		this.prezzoBiglietto = prezzoBiglietto;
	}		
}


