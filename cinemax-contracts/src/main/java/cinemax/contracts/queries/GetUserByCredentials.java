package cinemax.contracts.queries;

import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.UserRequest;

public class GetUserByCredentials implements Query, UserRequest  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private String username;	
	private String md5Password;

	public GetUserByCredentials() {
	}
	
	public GetUserByCredentials(String username, String md5Password) {
		this.username = username;
		this.md5Password = md5Password;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMd5Password() {
		return md5Password;
	}
	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}
	
	
}
 