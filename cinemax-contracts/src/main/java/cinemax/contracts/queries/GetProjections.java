/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.contracts.queries;

import java.math.BigDecimal;
import java.time.LocalDate;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;

/**
 * Query per la ricerca e il filtraggio avanzato delle proiezioni cinematografiche.
 * <p>
 * Incapsula molteplici criteri di ricerca (titolo del film, genere cinematografico,
 * intervallo temporale delle date e intervallo di prezzo del biglietto) inviati dal client al server.
 * I parametri non valorizzati (pari a {@code null}) non vengono applicati come filtri.
 * </p>
 */
public class GetProjections implements Query, ProjectionRequest {

	/**
	 * Identificatore di versione per la serializzazione della classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Titolo o porzione di titolo del film da ricercare.
	 */
	private String titolo;

	/**
	 * Genere cinematografico su cui filtrare le proiezioni.
	 */
	private String genere;

	/**
	 * Data iniziale (inclusa) dell'intervallo temporale delle proiezioni.
	 */
	private LocalDate daDataProiezione;

	/**
	 * Data finale (inclusa) dell'intervallo temporale delle proiezioni.
	 */
	private LocalDate aDataProiezione;

	/**
	 * Prezzo minimo (incluso) del biglietto per il filtraggio.
	 */
	private BigDecimal daCosto;

	/**
	 * Prezzo massimo (incluso) del biglietto per il filtraggio.
	 */
	private BigDecimal aCosto;
	
	/**
	 * Costruttore predefinito senza argomenti.
	 * <p>
	 * Necessario per framework di serializzazione, deserializzazione o binding via socket TCP.
	 * </p>
	 */
	public GetProjections() {
	}
	
	/**
	 * Costruttore parametrizzato per inizializzare tutti i criteri di filtro della query.
	 *
	 * @param titolo           il titolo o porzione di titolo del film (può essere {@code null})
	 * @param genere           il genere cinematografico (può essere {@code null})
	 * @param daDataProiezione la data di inizio intervallo (può essere {@code null})
	 * @param aDataProiezione  la data di fine intervallo (può essere {@code null})
	 * @param daCosto          il costo minimo del biglietto (può essere {@code null})
	 * @param aCosto           il costo massimo del biglietto (può essere {@code null})
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
	 * Restituisce il titolo del film impostato come filtro di ricerca.
	 *
	 * @return il titolo del film, oppure {@code null} se non filtrato
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * Imposta il titolo del film come criterio di ricerca.
	 *
	 * @param titolo il titolo o porzione di esso da impostare come filtro
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	/**
	 * Restituisce il genere cinematografico impostato come filtro.
	 *
	 * @return il genere del film, oppure {@code null} se non filtrato
	 */
	public String getGenere() {
		return genere;
	}

	/**
	 * Imposta il genere cinematografico come criterio di ricerca.
	 *
	 * @param genere il genere del film
	 */
	public void setGenere(String genere) {
		this.genere = genere;
	}
	
	/**
	 * Restituisce il costo minimo impostato come filtro.
	 *
	 * @return il prezzo minimo come {@link BigDecimal}, oppure {@code null} se non impostato
	 */
	public BigDecimal getDaCosto() {
		return daCosto;
	}

	/**
	 * Imposta la soglia minima di prezzo per il biglietto.
	 *
	 * @param daCosto il costo minimo da impostare come filtro
	 */
	public void setDaCosto(BigDecimal daCosto) {
		this.daCosto = daCosto;
	}

	/**
	 * Restituisce il costo massimo impostato come filtro.
	 *
	 * @return il prezzo massimo come {@link BigDecimal}, oppure {@code null} se non impostato
	 */
	public BigDecimal getaCosto() {
		return aCosto;
	}

	/**
	 * Imposta la soglia massima di prezzo per il biglietto.
	 *
	 * @param aCosto il costo massimo da impostare come filtro
	 */
	public void setaCosto(BigDecimal aCosto) {
		this.aCosto = aCosto;
	}

	/**
	 * Restituisce la data finale dell'intervallo di ricerca.
	 *
	 * @return la data di fine intervallo come {@link LocalDate}, oppure {@code null} se non impostata
	 */
	public LocalDate getaDataProiezione() {
		return aDataProiezione;
	}

	/**
	 * Imposta la data finale dell'intervallo temporale di ricerca.
	 *
	 * @param aDataProiezione la data di fine intervallo da impostare
	 */
	public void setaDataProiezione(LocalDate aDataProiezione) {
		this.aDataProiezione = aDataProiezione;
	}

	/**
	 * Restituisce la data iniziale dell'intervallo di ricerca.
	 *
	 * @return la data di inizio intervallo come {@link LocalDate}, oppure {@code null} se non impostata
	 */
	public LocalDate getDaDataProiezione() {
		return daDataProiezione;
	}

	/**
	 * Imposta la data iniziale dell'intervallo temporale di ricerca.
	 *
	 * @param daDataProiezione la data di inizio intervallo da impostare
	 */
	public void setDaDataProiezione(LocalDate daDataProiezione) {
		this.daDataProiezione = daDataProiezione;
	}
}

