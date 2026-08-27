/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.ProjectionRequest;

/**
 * Comando per la creazione o l'aggiornamento di una proiezione cinematografica nel sistema.
 * <p>
 * Incapsula le informazioni necessarie (data e ora, film associato, prezzo del biglietto
 * ed eventuale identificativo) per l'elaborazione e la persistenza della proiezione lato server.
 * </p>
 */
public class StoreProjection implements Command, ProjectionRequest { 
	
	private static final long serialVersionUID = 1L;

	/**
	 * Identificatore univoco della proiezione
	 */
	private Integer id;

	/**
	 * Data e ora programmate per la proiezione.
	 */
	private LocalDateTime DataOraProiezione;

	/**
	 * Identificatore univoco del film associato alla proiezione.
	 */
	private Integer idFilm;

	/**
	 * Prezzo unitario del biglietto per assistere alla proiezione.
	 */
	private BigDecimal prezzoBiglietto;
	
	/**
	 * Costruttore predefinito senza argomenti.
	 * <p>
	 * Necessario per framework di serializzazione, deserializzazione o binding automatico.
	 * </p>
	 */
	public StoreProjection() {
	}
	
	/**
	 * Costruttore per la modifica di una proiezione esistente (aggiornamento).
	 *
	 * @param id                l'identificatore univoco della proiezione da aggiornare
	 * @param dataOraProiezione la data e l'ora pianificate per la proiezione
	 * @param idFilm            l'identificatore univoco del film associato
	 * @param prezzoBiglietto   il prezzo unitario del biglietto
	 */
	public StoreProjection(Integer id, LocalDateTime dataOraProiezione, Integer idFilm, BigDecimal prezzoBiglietto) {
		super();
		this.id = id;
		this.DataOraProiezione = dataOraProiezione;
		this.idFilm = idFilm;
		this.prezzoBiglietto = prezzoBiglietto;
	}
	
	/**
	 * Costruttore per la creazione di una nuova proiezione (inserimento).
	 * <p>
	 * L'identificativo viene impostato a {@code null} poiché verrà generato dal database.
	 * </p>
	 *
	 * @param dataOraProiezione la data e l'ora pianificate per la proiezione
	 * @param idFilm            l'identificatore univoco del film associato
	 * @param prezzoBiglietto   il prezzo unitario del biglietto
	 */
	public StoreProjection(LocalDateTime dataOraProiezione, Integer idFilm, BigDecimal prezzoBiglietto) {
		super();
		this.id = null;
		this.DataOraProiezione = dataOraProiezione;
		this.idFilm = idFilm;
		this.prezzoBiglietto = prezzoBiglietto;
	}
	
	/**
	 * Restituisce l'identificatore univoco della proiezione.
	 *
	 * @return l'id della proiezione, oppure null se non disponibile
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore univoco della proiezione.
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Restituisce la data e l'ora previste per la proiezione.
	 *
	 * @return la data e ora della proiezione come {@link LocalDateTime}
	 */
	public LocalDateTime getDataOraProiezione() {
		return DataOraProiezione;
	}

	/**
	 * Imposta la data e l'ora previste per la proiezione.
	 *
	 * @param dataOraProiezione
	 */
	public void setDataOraProiezione(LocalDateTime dataOraProiezione) {
		this.DataOraProiezione = dataOraProiezione;
	}

	/**
	 * Restituisce l'identificatore del film associato alla proiezione.
	 *
	 * @return l'identificatore del film
	 */
	public Integer getIdFilm() {
		return idFilm;
	}

	/**
	 * Imposta l'identificatore del film associato alla proiezione.
	 *
	 * @param idFilm
	 */
	public void setIdFilm(Integer idFilm) {
		this.idFilm = idFilm;
	}

	/**
	 * Restituisce il prezzo del biglietto per la proiezione.
	 *
	 * @return il prezzo del biglietto come {@link BigDecimal}
	 */
	public BigDecimal getPrezzoBiglietto() {
		return prezzoBiglietto;
	}

	/**
	 * Imposta il prezzo del biglietto per la proiezione.
	 *
	 * @param prezzoBiglietto
	 */
	public void setPrezzoBiglietto(BigDecimal prezzoBiglietto) {
		this.prezzoBiglietto = prezzoBiglietto;
	}		
}
