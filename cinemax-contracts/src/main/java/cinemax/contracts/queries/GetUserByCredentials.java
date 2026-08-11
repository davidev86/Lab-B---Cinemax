package cinemax.contracts.queries;

import cinemax.contracts.interfaces.*;

public class GetUserByCredentials implements Query, UserRequest  {

	private String username;	
	private String md5Password;
	
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
 