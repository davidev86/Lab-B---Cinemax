package cinemax.serverCM.dao;

import cinemax.contracts.interfaces.Command;

import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetProjectionById;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmAndDate;
import cinemax.contracts.queries.GetProjectionsByRangeDate;
import cinemax.contracts.responses.GetProjectionResponse;
import cinemax.contracts.responses.GetProjectionsResponse;
import cinemax.contracts.responses.StoreProjectionResponse;
import cinemax.serverCM.dao.utils.DbHelper;
import cinemax.serverCM.dao.utils.SqlInsertBuilder;
import cinemax.serverCM.dao.utils.SqlQueryBuilder;
import cinemax.serverCM.dao.utils.SqlUpdateBuilder;
import cinemax.contracts.commands.StoreProjection;
import cinemax.contracts.dto.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

public class ProjectionDao implements Dao {

	private Connection _connection; 

	public ProjectionDao(Connection connection) {
		_connection = connection;
	}

	//il tipo di ritorno deve essere
	@Override
	public Response find(Query req){

		Response response = null;
		try { 

			switch (req) {
			case GetProjections u  -> response =find(u);  
			case GetProjectionsByFilmAndDate u  -> response = find(u);
			case GetProjectionById u  -> response = find(u);  
			case GetProjectionsByRangeDate u  -> response = find(u);
			default -> throw new IllegalArgumentException("Unexpected value: " + req);

			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}



	private Response find(GetProjections req) {
	
		String baseQuery = "SELECT * FROM public.\"Proiezioni_pianificate\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);
	
		String titoloPattern = (req.getTitolo() != null && !req.getTitolo().isBlank()) 
				? "%" + req.getTitolo() + "%" 
				: null;
		
		sqb.and("titolofilm ILIKE ?", titoloPattern)
		.and("genere ILIKE ?", req.getGenere())
		.and("data_ora_proiezione >= ?", req.getDaDataProiezione())
		.and("data_ora_proiezione < ?", req.getaDataProiezione())
		.and("prezzo_biglietto >= ?", req.getDaCosto())
		.and("prezzo_biglietto < ?", req.getaCosto());				

		try {

			List<ProjectionDetails> projs = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				ProjectionDetails dto = new ProjectionDetails();
				dto.setId(rs.getInt("id_proiezione"));
				dto.setIdFilm(rs.getInt("id_film"));
				dto.setDataOraProiezione(rs.getObject("data_ora_proiezione", LocalDateTime.class));
				dto.setTitoloFilm(rs.getString("titolofilm"));
				dto.setGenere(rs.getString("genere"));
				dto.setRegista(rs.getString("regista"));
				dto.setAnno(rs.getInt("anno"));
				dto.setDurataMinuti(rs.getInt("durataminuti"));
				dto.setEtaMinima(rs.getInt("etaminima"));
				dto.setCosto(rs.getBigDecimal("prezzo_biglietto"));
				dto.setTotalePostiPrenotati(rs.getInt("totale_posti_prenotati"));
				return dto;
			} );

			return new GetProjectionsResponse(projs);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Response find(GetProjectionsByFilmAndDate req) {		

		String baseQuery = "SELECT * FROM public.\"Proiezioni_pianificate\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		String titoloPattern = (req.getTitoloFilm() != null && !req.getTitoloFilm().isBlank()) 
				? "%" + req.getTitoloFilm() + "%" 
				: null;
		
		sqb.and("titolofilm ILIKE ?", titoloPattern)
		.and("data_ora_proiezione <= ?", req.getMaxDataPrenotazione());				

		try {

			List<ProjectionDetails> projs = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				ProjectionDetails dto = new ProjectionDetails();
				dto.setId(rs.getInt("id_proiezione"));
				dto.setIdFilm(rs.getInt("id_film"));
				dto.setDataOraProiezione(rs.getObject("data_ora_proiezione", LocalDateTime.class));
				dto.setTitoloFilm(rs.getString("titolofilm"));
				dto.setGenere(rs.getString("genere"));
				dto.setRegista(rs.getString("regista"));
				dto.setAnno(rs.getInt("anno"));
				dto.setDurataMinuti(rs.getInt("durataminuti"));
				dto.setEtaMinima(rs.getInt("etaminima"));
				dto.setCosto(rs.getBigDecimal("prezzo_biglietto"));
				dto.setTotalePostiPrenotati(rs.getInt("totale_posti_prenotati"));
				return dto;
			} );

			return new GetProjectionsResponse(projs);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	
	private Response find(GetProjectionById req) {		

		String baseQuery = "SELECT * FROM public.\"Proiezioni_pianificate\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		
		sqb.and("id_proiezione = ?", req.getIdProiezione());
	
		try {

			List<ProjectionDetails> projs = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				ProjectionDetails dto = new ProjectionDetails();
				dto.setId(rs.getInt("id_proiezione"));
				dto.setIdFilm(rs.getInt("id_film"));
				dto.setDataOraProiezione(rs.getObject("data_ora_proiezione", LocalDateTime.class));
				dto.setTitoloFilm(rs.getString("titolofilm"));
				dto.setGenere(rs.getString("genere"));
				dto.setRegista(rs.getString("regista"));
				dto.setAnno(rs.getInt("anno")); 
				dto.setDurataMinuti(rs.getInt("durataminuti"));
				dto.setEtaMinima(rs.getInt("etaminima"));
				dto.setCosto(rs.getBigDecimal("prezzo_biglietto"));
				dto.setTotalePostiPrenotati(rs.getInt("totale_posti_prenotati"));
				return dto;
			} );

			return new GetProjectionResponse(projs.getFirst());

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private Response find(GetProjectionsByRangeDate req) {
		
		String baseQuery = "SELECT * FROM public.\"Proiezioni_pianificate\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);
		
		sqb
		.and("data_ora_proiezione >= ?", req.getDaDataProiezione())
		.and("data_ora_proiezione < ?", req.getaDataProiezione());				

		try {

			List<ProjectionDetails> projs = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				ProjectionDetails dto = new ProjectionDetails();
				dto.setId(rs.getInt("id_proiezione"));
				dto.setIdFilm(rs.getInt("id_film"));
				dto.setDataOraProiezione(rs.getObject("data_ora_proiezione", LocalDateTime.class));
				dto.setTitoloFilm(rs.getString("titolofilm"));
				dto.setGenere(rs.getString("genere"));
				dto.setRegista(rs.getString("regista"));
				dto.setAnno(rs.getInt("anno"));
				dto.setDurataMinuti(rs.getInt("durataminuti"));
				dto.setEtaMinima(rs.getInt("etaminima"));
				dto.setCosto(rs.getBigDecimal("prezzo_biglietto"));
				dto.setTotalePostiPrenotati(rs.getInt("totale_posti_prenotati"));
				return dto;
			} );

			return new GetProjectionsResponse(projs);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	@Override
	public Response execute(Command req) {

		//CASO INSERT
		if(req.getId() == null ) return insertProjection((StoreProjection) req);		
		//CASO UPDATE
		else return updateProjection((StoreProjection)req);			
	}

	private Response updateProjection(StoreProjection req) {
		SqlUpdateBuilder sub = new SqlUpdateBuilder("public.\"Proiezioni\"");
		
		sub.set("data_ora_proiezione", req.getDataOraProiezione());
		sub.set("prezzo_biglietto", req.getPrezzoBiglietto());
		sub.set("id_film", req.getIdFilm());
		
		sub.where("id", req.getId());
		
		try {
			int rowsAffected = DbHelper.executeUpdate(_connection, sub.getSql(), sub.getParams());
			System.out.println("Proiezione aggiornata, righe modificate: " + rowsAffected);

			return new StoreProjectionResponse(req.getId());

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}

	private Response insertProjection(StoreProjection req) {
		SqlInsertBuilder sib = new SqlInsertBuilder("public.\"Proiezioni\"");

		sib.set("data_ora_proiezione", req.getDataOraProiezione())
		.set("prezzo_biglietto", req.getPrezzoBiglietto())
		.set("id_film", req.getIdFilm());	       

		try {
			// Esegue l'insert e recupera l'ID generato da PostgreSQL
			Integer newId = DbHelper.executeInsert(_connection, sib.getSql(), sib.getParams()); 
			System.out.println("Nuova proiezione inserita con ID: " + newId);

			return new StoreProjectionResponse(newId);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
