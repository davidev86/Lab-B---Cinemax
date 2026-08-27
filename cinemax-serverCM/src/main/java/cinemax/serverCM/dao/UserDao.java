/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
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
 * Data Access Object (DAO) responsabile per le operazioni di lettura, registrazione
 * e aggiornamento degli utenti nel database PostgreSQL all'interno della tabella {@code public."Utenti"}.
 * <p>
 * Fornisce il supporto all'autenticazione tramite credenziali (username e hash MD5 della password),
 * al recupero del profilo dettagliato utente ({@link UserDetails}) e alla persistenza dei dati anagrafici e di ruolo.
 * </p>
 */
public class UserDao implements Dao {

	private Connection _connection; 

	/**
	 * Costruisce il DAO per la gestione degli utenti associando la connessione JDBC attiva.
	 *
	 * @param connection la connessione aperta verso il database PostgreSQL
	 */
	public UserDao(Connection connection) {
		_connection = connection;
	}

	/**
	 * Instrada ed esegue una richiesta di tipo {@link Query} relativa agli utenti.
	 *
	 * @param req la query da processare (es. {@link GetUserByCredentials}, {@link GetUserDetails})
	 * @return l'istanza {@link Response} corrispondente al risultato dell'interrogazione, o {@code null} in caso di errore
	 * @throws IllegalArgumentException se il tipo di query fornito non è riconosciuto
	 */
	@Override
	public Response find(Query req){

		Response response = null;
		try {

			switch (req) {
			case GetUserByCredentials u  -> response = Find(u);  
			case GetUserDetails u  -> response = Find(u);
			default -> throw new IllegalArgumentException("Unexpected value: " + req);

			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Esegue l'autenticazione dell'utente ricercando la corrispondenza tra username (case-insensitive)
	 * e hash MD5 della password memorizzata.
	 *
	 * @param req la richiesta {@link GetUserByCredentials} contenente username e password hashata
	 * @return l'oggetto {@link GetUserByCredentialResponse} con le informazioni minime dell'utente ({@link UserMinInfo}),
	 *         oppure {@code null} se le credenziali non sono valide o in caso di errore SQL
	 */
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
	
	/**
	 * Recupera la scheda anagrafica e di profilo completa di un utente a partire dal suo identificativo univoco.
	 *
	 * @param req la richiesta {@link GetUserDetails} contenente l'identificativo numerico dell'utente
	 * @return l'oggetto {@link GetUserDetailsResponse} con i dettagli anagrafici ({@link UserDetails}), o {@code null} in caso di errore SQL
	 */
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
	                dto.setDataNascita(sqlDate.toLocalDate());
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

	/**
	 * Esegue un comando transazionale ({@link Command}) distinguendo tra operazione di registrazione
	 * di un nuovo utente (ID nullo) o modifica anagrafica di un utente esistente.
	 *
	 * @param cmd il comando da processare (istanza di {@link StoreUser})
	 * @return l'istanza {@link Response} risultante dall'esecuzione del comando
	 */
	@Override
	public Response execute(Command cmd) {	
		
		//CASO INSERT
		if(cmd.getId() == null ) return insertUser((StoreUser)cmd);
		//CASO UPDATE
		else return updateUser((StoreUser)cmd);
	}	
	
	/**
	 * Aggiorna le informazioni anagrafiche, di sicurezza e di ruolo di un utente esistente nella tabella {@code public."Utenti"}.
	 *
	 * @param req il comando {@link StoreUser} contenente i campi aggiornati e l'ID dell'utente
	 * @return l'oggetto {@link StoreUserResponse} con l'ID dell'utente modificato, o {@code null} in caso di errore SQL
	 */
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

	/**
	 * Inserisce un nuovo record utente nella tabella {@code public."Utenti"} a seguito della registrazione.
	 *
	 * @param req il comando {@link StoreUser} con tutti i parametri anagrafici e credenziali del nuovo account
	 * @return l'oggetto {@link StoreUserResponse} contenente l'ID generato dal database PostgreSQL, o {@code null} in caso di errore SQL
	 */
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