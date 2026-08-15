package cinemax.serverCM.services;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmIdAndDate;
import cinemax.contracts.responses.GetProjectionResponse;
import cinemax.contracts.responses.StoreProjectionResponse;
import cinemax.contracts.commands.StoreProjection;
import cinemax.contracts.dto.*;
import cinemax.serverCM.services.Utils.DbHelper;
import cinemax.serverCM.services.Utils.SqlInsertBuilder;
import cinemax.serverCM.services.Utils.SqlQueryBuilder;
import cinemax.serverCM.services.Utils.SqlUpdateBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.time.LocalDateTime;

public class ProjectionService {

	private Connection _connection; 

	public ProjectionService(Connection connection) {
		_connection = connection;
	}

	//il tipo di ritorno deve essere
	public Response Find(ProjectionRequest req) {

		Response response = null;
		try {

			switch (req) {
			case GetProjections u  -> response = Find(u);  
			case GetProjectionsByFilmIdAndDate u  -> response = Find(u);  
			default -> throw new IllegalArgumentException("Unexpected value: " + req);

			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}



	public Response Find(GetProjections req) {
	
		String baseQuery = "SELECT * FROM public.\"Proiezioni_pianificate\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("titolo ILIKE ?", req.getTitolo())
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
				return dto;
			} );

			return new GetProjectionResponse(projs);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Response Find(GetProjectionsByFilmIdAndDate req) {		

		String baseQuery = "SELECT * FROM public.\"Proiezioni_pianificate\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("idFilm = ?", req.getFilm())	
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
				return dto;
			} );

			return new GetProjectionResponse(projs);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public Response Store(StoreProjection req) {

		//CASO INSERT
		if(req.getId() == null ) return insertProjection(req);		
		//CASO UPDATE
		else return updateProjection(req);			
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
