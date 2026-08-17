package cinemax.contracts.commands;

import cinemax.contracts.interfaces.Command;

public class StoreBooking implements Command {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer idUtente;
	private Integer idProiezione;
	private Integer numeroPosti;

	public StoreBooking() {
	}

	// Costruttore per Inserimento (ID generato dal DB)
	public StoreBooking(Integer idUtente, Integer idProiezione, Integer numeroPosti) {
		this.idUtente = idUtente;
		this.idProiezione = idProiezione;
		this.numeroPosti = numeroPosti;
	}

	// Costruttore per Aggiornamento (ID esistente)
	public StoreBooking(Integer id, Integer idUtente, Integer idProiezione, Integer numeroPosti) {
		this.id = id;
		this.idUtente = idUtente;
		this.idProiezione = idProiezione;
		this.numeroPosti = numeroPosti;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public Integer getIdProiezione() {
		return idProiezione;
	}

	public void setIdProiezione(Integer idProiezione) {
		this.idProiezione = idProiezione;
	}

	public Integer getNumeroPosti() {
		return numeroPosti;
	}

	public void setNumeroPosti(Integer numeroPosti) {
		this.numeroPosti = numeroPosti;
	}
}