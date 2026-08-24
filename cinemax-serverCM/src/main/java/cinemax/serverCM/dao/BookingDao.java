package cinemax.serverCM.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import cinemax.contracts.commands.DeleteBooking;
import cinemax.contracts.commands.StoreBooking;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetBookings;
import cinemax.contracts.queries.GetBookingsByDate;
import cinemax.contracts.queries.GetBookingsByUserId;
import cinemax.contracts.responses.DeleteBookingResponse;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.StoreBookingResponse;
import cinemax.serverCM.dao.utils.DbHelper;
import cinemax.serverCM.dao.utils.SqlDeleteBuilder;
import cinemax.serverCM.dao.utils.SqlInsertBuilder;
import cinemax.serverCM.dao.utils.SqlQueryBuilder;
import cinemax.serverCM.dao.utils.SqlUpdateBuilder;

public class BookingDao implements Dao {

	private final Connection _connection;

	public BookingDao(Connection connection) {
		this._connection = connection;
	}

	@Override
	public Response find(Query req) {
		Response response = null;
		try {
			switch (req) {
				case GetBookings u -> response = find(u);
				case GetBookingsByUserId u -> response = find(u);
				case GetBookingsByDate u -> response = find(u);
				default -> throw new IllegalArgumentException("Unexpected value: " + req);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	// Ricerca avanzata (codice, nome, cognome, titolo film, intervallo date)
	private Response find(GetBookings req) {
		String baseQuery = "SELECT * FROM public.\"Prenotazioni_pianificate\"";
		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("id_prenotazione = ?", req.getCodicePrenotazione())
		   .and("nome ILIKE ?", req.getNomeCliente())
		   .and("cognome ILIKE ?", req.getCognomeCliente());

		String titoloPattern = (req.getTitoloFilm() != null && !req.getTitoloFilm().isBlank()) 
				? "%" + req.getTitoloFilm() + "%" 
				: null;
		sqb.and("titolofilm ILIKE ?", titoloPattern);

		sqb.and("data_ora_proiezione >= ?", req.getDaDataProiezione())
		   .and("data_ora_proiezione <= ?", req.getADataProiezione());

		return executeBookingQuery(sqb);
	}

	// Ricerca per id utente 
	private Response find(GetBookingsByUserId req) {
		String baseQuery = "SELECT * FROM public.\"Prenotazioni_pianificate\"";
		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("id_utente = ?", req.getIdUtente());

		return executeBookingQuery(sqb);
	}
	
	// Ricerca le prenotazioni per una specifica data ricevuta nella request
	private Response find(GetBookingsByDate req) {
	    LocalDate targetDate = (req != null && req.getDate() != null)  
	            ? req.getDate() 
	            : LocalDate.now();

	    LocalDateTime startOfDay = targetDate.atStartOfDay();
	    LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

	    String baseQuery = "SELECT * FROM public.\"Prenotazioni_pianificate\"";
	    SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

	    sqb.and("data_ora_proiezione >= ?", startOfDay)
	       .and("data_ora_proiezione <= ?", endOfDay);

	    return executeBookingQuery(sqb);
	}

	private Response executeBookingQuery(SqlQueryBuilder sqb) {
		try {
			List<BookingDetails> bookings = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				BookingDetails dto = new BookingDetails();
				dto.setIdPrenotazione(rs.getInt("id_prenotazione"));
				dto.setIdProiezione(rs.getInt("id_proiezione"));
				dto.setIdUtente(rs.getInt("id_utente"));
				dto.setNomeCliente(rs.getString("nome"));
				dto.setCognomeCliente(rs.getString("cognome"));
				dto.setTitoloFilm(rs.getString("titolofilm"));
				dto.setDataOraProiezione(rs.getObject("data_ora_proiezione", LocalDateTime.class));
				dto.setNumeroPosti(rs.getInt("num_posti"));
				dto.setCosto(rs.getBigDecimal("prezzo_biglietto"));
				dto.setTotale(rs.getBigDecimal("totale"));
				return dto;
			});

			return new GetBookingResponse(bookings);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Response execute(Command req) {
		return switch (req) {
			case StoreBooking storeBooking -> (storeBooking.getId() == null) 
					? insertBooking(storeBooking) 
					: updateBooking(storeBooking);
			case DeleteBooking deleteBooking -> deleteBooking(deleteBooking);
			default -> null;
		};
	}

	private Response insertBooking(StoreBooking req) {
		SqlInsertBuilder sib = new SqlInsertBuilder("public.\"Prenotazioni\"");

		sib.set("id_utente", req.getIdUtente())
		   .set("id_proiezione", req.getIdProiezione())
		   .set("numero_posti", req.getNumeroPosti());

		try {
			Integer newId = DbHelper.executeInsert(_connection, sib.getSql(), sib.getParams());
			return new StoreBookingResponse(newId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Response updateBooking(StoreBooking req) {
		SqlUpdateBuilder sub = new SqlUpdateBuilder("public.\"Prenotazioni\"");

		sub.set("id_utente", req.getIdUtente());
		sub.set("id_proiezione", req.getIdProiezione());
		sub.set("numero_posti", req.getNumeroPosti());
		sub.where("id", req.getId());

		try {
			DbHelper.executeUpdate(_connection, sub.getSql(), sub.getParams());
			return new StoreBookingResponse(req.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Response deleteBooking(DeleteBooking req) {
		SqlDeleteBuilder sdb = new SqlDeleteBuilder("public.\"Prenotazioni\"");
		sdb.where("id = ?", req.getId());

		try {
			int rowsAffected = DbHelper.executeUpdate(_connection, sdb.getSql(), sdb.getParams());
			return new DeleteBookingResponse(rowsAffected > 0);
		} catch (SQLException e) {
			e.printStackTrace(); 
			return new DeleteBookingResponse(false);
		} 
	}
}