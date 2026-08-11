package cinemax.contracts.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.ProjectionRequest;

public class StoreProjection implements Command, ProjectionRequest{	
	
	private Integer id;
	private LocalDateTime DataOraProiezione;
	private Integer idFilm;
	private BigDecimal prezzoBiglietto;
	
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
