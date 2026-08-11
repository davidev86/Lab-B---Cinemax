package cinemax.serverCM.services;

import cinemax.contracts.interfaces.Response;
import cinemax.contracts.interfaces.UserRequest;
import cinemax.contracts.queries.GetUserByCredentials;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.contracts.dto.*;
import cinemax.serverCM.services.Utils.DbHelper;
import cinemax.serverCM.services.Utils.SqlInsertBuilder;
import cinemax.serverCM.services.Utils.SqlQueryBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

public class UserService {

	private Connection _connection; 

	public UserService(Connection connection) {
		_connection = connection;
	}

	//il tipo di ritorno deve essere
	public Response Find(UserRequest req) {

		Response response = null;
		try {

			switch (req) {
			case GetUserByCredentials u  -> response =  Find(u);  
			default -> throw new IllegalArgumentException("Unexpected value: " + req);

			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return response;
	}



	public Response Find(GetUserByCredentials req) {	

		String baseQuery = "SELECT * FROM public.\"Utenti\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("username = ?", req.getUsername())
		.and("password ILIKE ?", req.getMd5Password());				

		try {

			//one user is expected
			List<UserMinInfos> users = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				UserMinInfos dto = new UserMinInfos();
				dto.setId(rs.getInt("id"));
				dto.setUsername(rs.getString("username"));
				dto.setNome(rs.getString("nome"));
				dto.setCognome(rs.getString("cognome"));		
								
				return dto;
			} ); 
			
			if(users.size() == 0)
				return null;

			return new GetUserByCredentialResponse(users.getFirst());
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

		if(req.getId() == null ) {

			//CASO INSERT
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
		//CASO UPDATE


		return null;
	}
}
