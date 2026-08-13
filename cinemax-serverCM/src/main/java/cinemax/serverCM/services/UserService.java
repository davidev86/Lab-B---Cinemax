package cinemax.serverCM.services;

import cinemax.contracts.interfaces.Response;
import cinemax.contracts.interfaces.UserRequest;
import cinemax.contracts.queries.GetUserByCredentials;
import cinemax.contracts.queries.GetUserDetails;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.contracts.responses.GetUserDetailsResponse;
import cinemax.contracts.dto.*;
import cinemax.contracts.dto.Enums.Ruolo;
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
	
	public Response Find(GetUserDetails req) {		

		String baseQuery = "SELECT * FROM public.\"Utenti\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("id = ?", req.getUserId());				

		sqb.and("id = ?", req.getUserId());				

	    try {

	        List<UserDetails> users = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
	            UserDetails dto = new UserDetails();
	            
	            // 1. ID (Corretto da 'id_proiezione' a 'id')
	            dto.setId(rs.getInt("id"));
	            
	            // 2. Anagrafica e Credenziali
	            dto.setNome(rs.getString("nome"));
	            dto.setCognome(rs.getString("cognome"));
	            dto.setUsername(rs.getString("username"));
	            
	            // 3. Domicilio e Ruolo
	            dto.setDomicilio(rs.getString("domicilio"));
	        
	            // Ruolo (conversione da String del DB a Enum Java)
	            String ruoloStr = rs.getString("ruolo");
	            if (ruoloStr != null) {
	                dto.setRuolo(Ruolo.valueOf(ruoloStr)); 
	            }
	            
	            // 4. Data di nascita (gestisce eventuali valori NULL sul DB)
	            java.sql.Date sqlDate = rs.getDate("data_nascita");
	            if (sqlDate != null) {
	                dto.setDataNascita(sqlDate.toLocalDate()); // Se in UserDetails usi LocalDate
	                // dto.setDataNascita(sqlDate);             // Se in UserDetails usi java.util.Date / java.sql.Date
	            }
	            
	            return dto;
	        });

	        return new GetUserDetailsResponse(users.getFirst());

	    	

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/*
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
	*/
}
