package cinemax.serverCM.dao;

import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;

public interface Dao {

	//il tipo di ritorno deve essere
	Response find(Query req);

	Response store(Command req); 

}