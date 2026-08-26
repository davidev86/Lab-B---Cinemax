/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.serverCM.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cinemax.contracts.commands.StoreUser;
import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.dto.UserDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetUserByCredentials;
import cinemax.contracts.queries.GetUserDetails;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.contracts.responses.GetUserDetailsResponse;
import cinemax.contracts.responses.StoreUserResponse;
import cinemax.serverCM.dao.utils.DbHelper;
import cinemax.serverCM.dao.utils.SqlInsertBuilder;
import cinemax.serverCM.dao.utils.SqlQueryBuilder;
import cinemax.serverCM.dao.utils.SqlUpdateBuilder;

/**
 * DAO per la gestione delle operazioni di lettura, creazione e modifica degli utenti nel database, incluse autenticazione e recupero dati profilo.
 */
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
		
		sqb.and("LOWER(username) = LOWER(?)", req.getUsername())
		.and("password = ?", req.getMd5Password());				

		try {

			//one user is expected
			List<UserMinInfo> users = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				UserMinInfo dto = new UserMinInfo();
				dto.setId(rs.getInt("id"));
				dto.setUsername(rs.getString("username"));
				dto.setNome(rs.getString("nome"));
				dto.setCognome(rs.getString("cognome"));		
				
				// Ruolo (conversione da String del DB a Enum Java)

	            String ruoloStr = rs.getString("ruolo");
	            if (ruoloStr != null) {
	                dto.setRuolo(Ruolo.fromDbValue(ruoloStr)); 
	            }
								
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
	            
	            
	            dto.setId(rs.getInt("id"));
	            
	            
	            dto.setNome(rs.getString("nome"));
	            dto.setCognome(rs.getString("cognome"));
	            dto.setUsername(rs.getString("username"));
	            
	            
	            dto.setDomicilio(rs.getString("domicilio"));
	        
	            // Ruolo (conversione da String del DB a Enum Java)
	            String ruoloStr = rs.getString("ruolo");
	            if (ruoloStr != null) {
	                dto.setRuolo(Ruolo.fromDbValue(ruoloStr)); 
	            }
	            
	            
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
	public Response execute(Command cmd) {	
		
		//CASO INSERT
		if(cmd.getId() == null ) return insertUser((StoreUser)cmd);
		//CASO UPDATE
		else return updateUser((StoreUser)cmd);
	}	
	
	private Response updateUser(StoreUser req) {
	    SqlUpdateBuilder sub = new SqlUpdateBuilder("public.\"Utenti\"");
	    
	    sub.set("username", req.getUsername())
	       .set("password", req.getMd5Password())
	       .set("nome", req.getNome())
	       .set("cognome", req.getCognome())
	       .set("data_nascita", req.getDataNascita())
	       .set("domicilio", req.getDomicilio())
	       .set("ruolo", req.getRuolo() != null ? req.getRuolo().name().toLowerCase() : null);         

	    sub.where("id", req.getId());
	    
	    try {
	        int rowsAffected = DbHelper.executeUpdate(_connection, sub.getSql(), sub.getParams());
	        System.out.println("Utente aggiornato, righe modificate: " + rowsAffected);

	        return new StoreUserResponse(req.getId());

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }       
	}

	private Response insertUser(StoreUser req) {
	    SqlInsertBuilder sib = new SqlInsertBuilder("public.\"Utenti\"");

	    sib.set("username", req.getUsername())
	       .set("password", req.getMd5Password())
	       .set("nome", req.getNome())
	       .set("cognome", req.getCognome())
	       .set("data_nascita", req.getDataNascita())
	       .set("domicilio", req.getDomicilio())
	       .set("ruolo", req.getRuolo() != null ? req.getRuolo().name().toLowerCase() : null);         

	    try {
	        // Esegue l'insert e recupera l'ID generato da PostgreSQL
	        Integer newId = DbHelper.executeInsert(_connection, sib.getSql(), sib.getParams()); 
	        System.out.println("Nuovo utente inserito con ID: " + newId);

	        return new StoreUserResponse(newId);

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }           
	}
}


