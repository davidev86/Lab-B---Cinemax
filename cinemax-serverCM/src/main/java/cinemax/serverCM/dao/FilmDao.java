package cinemax.serverCM.dao;

import cinemax.contracts.interfaces.Command;

import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetFilmsByTitle;
import cinemax.contracts.queries.GetProjectionById;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmAndDate;
import cinemax.contracts.responses.GetFilmsResponse;
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

public class FilmDao implements Dao {

	private Connection _connection; 

	public FilmDao(Connection connection) {
		_connection = connection;
	}

	//il tipo di ritorno deve essere
	@Override
	public Response find(Query req){

		Response response = null;
		try { 

			switch (req) {
			case GetFilmsByTitle u  -> response =find(u);  			
			default -> throw new IllegalArgumentException("Unexpected value: " + req);

			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}



	private Response find(GetFilmsByTitle req) {
	
		String baseQuery = "SELECT * FROM public.\"Films\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);
	
		String titoloPattern = (req.getTitoloFilm() != null && !req.getTitoloFilm().isBlank()) 
				? "%" + req.getTitoloFilm() + "%" 
				: null;
		
		sqb.and("titolo_film ILIKE ?", titoloPattern);
		

		try {

			List<FilmDetails> films = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				FilmDetails dto = new FilmDetails();
				dto.setId(rs.getInt("id"));
			    dto.setTitoloFilm(rs.getString("titolo_film"));
			    dto.setGenere(rs.getString("genere"));
			    dto.setRegista(rs.getString("regista"));
			    dto.setAnno(rs.getInt("anno"));
			    dto.setDurataMinuti(rs.getInt("durata_minuti"));
			    dto.setEtaMinima(rs.getInt("eta_minima"));
				return dto;
			} );
					   
			return new GetFilmsResponse(films);

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
