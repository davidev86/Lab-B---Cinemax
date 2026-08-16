package cinemax.serverCM.dao;

import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetUserByCredentials;
import cinemax.contracts.queries.GetUserDetails;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.contracts.responses.GetUserDetailsResponse;
import cinemax.contracts.responses.StoreProjectionResponse;
import cinemax.contracts.responses.StoreUserResponse;
import cinemax.serverCM.dao.Utils.DbHelper;
import cinemax.serverCM.dao.Utils.SqlInsertBuilder;
import cinemax.serverCM.dao.Utils.SqlQueryBuilder;
import cinemax.serverCM.dao.Utils.SqlUpdateBuilder;
import cinemax.contracts.commands.StoreUser;
import cinemax.contracts.dto.*;
import cinemax.contracts.dto.Enums.Ruolo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements Dao {

	private Connection _connection; 

	public UserDao(Connection connection) {
		_connection = connection;
	}

	//il tipo di ritorno deve essere
	@Override
	public Response find(Query req){

		Response response = null;
		try {

			switch (req) {
			case GetUserByCredentials u  -> response =  Find(u);  
			case GetUserDetails u  -> response = Find(u);
			default -> throw new IllegalArgumentException("Unexpected value: " + req);

			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return response;
	}



	private Response Find(GetUserByCredentials req) {	

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
	
	private Response Find(GetUserDetails req) {		

		String baseQuery = "SELECT * FROM public.\"Utenti\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

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

	
	@Override
	public Response store(Command cmd) {	
		
		//CASO INSERT
		if(cmd.getId() == null ) return insertUser((StoreUser)cmd);
		//CASO UPDATE
		else return updateUser((StoreUser)cmd);
	}	
	
	private Response updateUser(StoreUser req) {
		SqlUpdateBuilder sub = new SqlUpdateBuilder("public.\"Utenti\"");
		
		sub.set("username", req.getUsername())
		   .set("md5Password", req.getMd5Password())
		   .set("nome", req.getNome())
		   .set("cognome", req.getCognome())
		   .set("dataNascita", req.getDataNascita())
		   .set("domicilio", req.getDomicilio())
		   .set("ruolo", req.getRuolo() != null ? req.getRuolo().name() : null);			

		
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
	
	private Response insertUser(StoreUser req) {
		//CASO INSERT
		SqlInsertBuilder sib = new SqlInsertBuilder("public.\"Utenti\"");

		sib.set("username", req.getUsername())
		   .set("md5Password", req.getMd5Password())
		   .set("nome", req.getNome())
		   .set("cognome", req.getCognome())
		   .set("dataNascita", req.getDataNascita())
		   .set("domicilio", req.getDomicilio())
		   .set("ruolo", req.getRuolo() != null ? req.getRuolo().name() : null);			

		try {
			// Esegue l'insert e recupera l'ID generato da PostgreSQL
			Integer newId = DbHelper.executeInsert(_connection, sib.getSql(), sib.getParams()); 
			System.out.println("Nuova proiezione inserita con ID: " + newId);

			return new StoreUserResponse(newId);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}			
	}
}
