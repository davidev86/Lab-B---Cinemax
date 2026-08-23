package cinemax.application.services;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import cinemax.application.services.utils.HashBuilder;
import cinemax.contracts.commands.StoreUser;
import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.queries.GetUserByCredentials;
import cinemax.contracts.queries.GetUserDetails;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.contracts.responses.GetUserDetailsResponse;
import cinemax.contracts.responses.StoreUserResponse;

public class UserService {

	private final TcpClient tcpClient;

    // Il costruttore richiede il client
    public UserService(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }
	
	public GetUserByCredentialResponse getUserByCredentials(String username, String password) {
		
		try {
			String md5Passord = HashBuilder.convertToMD5(password);
			GetUserByCredentials request = new GetUserByCredentials(username, md5Passord);		
			return tcpClient.sendRequest(request, GetUserByCredentialResponse.class);
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		}
	} 
	
	public GetUserDetailsResponse getUserDetails(int userId) {
		
		GetUserDetails request = new GetUserDetails(userId);		
		return tcpClient.sendRequest(request, GetUserDetailsResponse.class);
	}
	 
	
	public StoreUserResponse updateUser(Integer id, String username,	 String password, String nome, String cognome,	 LocalDate dataNascita,	 String domicilio,	 Ruolo ruolo) {
		
		try {
			String md5Passord = HashBuilder.convertToMD5(password);
			StoreUser request = new StoreUser(id, username,md5Passord,   nome,  cognome,	  dataNascita,	  domicilio,	  ruolo);		
			return tcpClient.sendRequest(request, StoreUserResponse.class);
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		}		
	}
	

		
	public StoreUserResponse insertUser(String username, String password, String nome, String cognome, LocalDate dataNascita, String domicilio) {
	    try {
	        String md5Password = HashBuilder.convertToMD5(password);
	        StoreUser request = new StoreUser(username, md5Password, nome, cognome, dataNascita, domicilio, Ruolo.CLIENTE);        
	        
	        StoreUserResponse response = tcpClient.sendRequest(request, StoreUserResponse.class);
	        
	        // Verifica se la risposta è valida e se l'operazione ha avuto successo
	        if (response == null || response.getId() == null) {
	            String msg = "Utente duplicato!";
	            throw new RuntimeException(msg);
	        }
	        
	        return response;

	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Errore nell'algoritmo di hashing della password.", e);
	    }
	}	
}
