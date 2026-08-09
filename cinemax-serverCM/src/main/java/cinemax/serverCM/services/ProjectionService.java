package cinemax.serverCM.services;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.requests.GetProjections;
import cinemax.contracts.responses.GetProjectionResponse;
import cinemax.contracts.dto.*;
import cinemax.serverCM.services.Utils.DbHelper;
import cinemax.serverCM.services.Utils.SqlQueryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.time.LocalDateTime;

public class ProjectionService {

	private Connection _connection; 

	public ProjectionService(Connection connection) {
		_connection = connection;
	}

	public GetProjectionResponse Find(ProjectionRequest req) {
		if(req instanceof GetProjections)
		{
			GetProjections getProjectionReq = (GetProjections) req;
			
			String baseQuery = "SELECT * FROM public.\"Proiezioni_pianificate\"";
			
			SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);
			
			sqb.and("titolo ILIKE ?", getProjectionReq.getTitolo())
			   .and("genere ILIKE ?", getProjectionReq.getGenere())
			   .and("data_ora_proiezione >= ?", getProjectionReq.getDaDataPrenotazione())
			   .and("data_ora_proiezione < ?", getProjectionReq.getDaDataPrenotazione())
			   .and("prezzo_biglietto >= ?", getProjectionReq.getDaCosto())
			   .and("prezzo_biglietto < ?", getProjectionReq.getaCosto());		
			

			Statement stmt;
			try {
				
				List<Projection> projs = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
					Projection dto = new Projection();
	                dto.setDataOraProiezione(rs.getObject("data_ora_proiezione", LocalDateTime.class));
	                dto.setTitoloFilm(rs.getString("titolofilm"));
	                dto.setGenere(rs.getString("genere"));
	                dto.setRegista(rs.getString("regista"));
	                dto.setAnno(rs.getInt("anno"));
	                dto.setDurataMinuti(rs.getInt("durataminuti"));
	                dto.setEtaMinima(rs.getInt("etaminima"));
	                return dto;
	            } );
				
				return new GetProjectionResponse(projs);
				
				
				// TODO: Crea l'oggetto Response, riempilo con i dati letti e invialo al client tramite oos.writeObject(response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}	

}
