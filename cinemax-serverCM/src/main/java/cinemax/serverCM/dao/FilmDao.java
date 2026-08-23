package cinemax.serverCM.dao;

import cinemax.contracts.interfaces.Command;

import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetFilmsById;
import cinemax.contracts.queries.GetFilmsByTitle;
import cinemax.contracts.queries.GetProjectionById;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.queries.GetProjectionsByFilmAndDate;
import cinemax.contracts.responses.GetFilmResponse;
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
			case GetFilmsById u  -> response =find(u);  
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
	
	private Response find(GetFilmsById req) {		

		String baseQuery = "SELECT * FROM public.\"Films\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("id = ?", req.getIdFilm());
	
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
					   
			return new GetFilmResponse(films.getFirst());

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Response execute(Command req) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Non è possibile inserire un film!");
	}	
}
