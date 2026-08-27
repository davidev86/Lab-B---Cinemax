/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
/**
 * @authors Francesca Pelizzoni, matricola 751550 (VA) e Davide Villa, matricola 701105 (VA)
 */
package cinemax.serverCM.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.queries.GetFilmsById;
import cinemax.contracts.queries.GetFilmsByTitle;
import cinemax.contracts.responses.GetFilmResponse;
import cinemax.contracts.responses.GetFilmsResponse;
import cinemax.serverCM.dao.utils.DbHelper;
import cinemax.serverCM.dao.utils.SqlQueryBuilder;

/**
 * Data Access Object (DAO) responsabile per le operazioni di lettura e interrogazione
 * sul catalogo dei film memorizzati nella tabella {@code public."Films"}.
 * <p>
 * Supporta l'esecuzione di query per la ricerca testuale per titolo ({@link GetFilmsByTitle})
 * e per identificativo univoco ({@link GetFilmsById}), mappando i record restituiti
 * dal database in istanze del DTO {@link FilmDetails}.
 * </p>
 */
public class FilmDao implements Dao {

	private Connection _connection; 

	/**
	 * Costruisce il DAO per la gestione dei film associando la connessione JDBC attiva.
	 *
	 * @param connection la connessione attiva verso il database PostgreSQL
	 */
	public FilmDao(Connection connection) {
		_connection = connection;
	}

	/**
	 * Instrada ed esegue una richiesta di tipo {@link Query} sul catalogo film,
	 * delegando la ricerca al metodo specifico in base al tipo concreto di query ricevuta.
	 *
	 * @param req la query da eseguire (es. {@link GetFilmsByTitle}, {@link GetFilmsById})
	 * @return l'istanza {@link Response} contenente i dati del film o la lista dei film trovati, oppure {@code null} in caso di errore
	 * @throws IllegalArgumentException se il tipo di query fornito non è gestito
	 */
	@Override
	public Response find(Query req){

		Response response = null;
		try { 

			switch (req) {
			case GetFilmsByTitle u  -> response = find(u); 	
			case GetFilmsById u  -> response = find(u);  
			default -> throw new IllegalArgumentException("Unexpected value: " + req);

			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Esegue la ricerca dei film nel catalogo filtrando per corrispondenza parziale del titolo (case-insensitive).
	 *
	 * @param req la richiesta {@link GetFilmsByTitle} contenente il pattern del titolo da ricercare
	 * @return l'oggetto {@link GetFilmsResponse} contenente la lista dei film corrispondenti, oppure {@code null} in caso di errore SQL
	 */
	private Response find(GetFilmsByTitle req) {
	
		String baseQuery = "SELECT * FROM public.\"Films\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);
	
		String titoloPattern = (req.getTitoloFilm() != null && !req.getTitoloFilm().isBlank()) 
				? "%" + req.getTitoloFilm() + "%" 
				: null;
		
		sqb.and("titolo_film ILIKE ?", titoloPattern);
		

		try {

			List<FilmDetails> films = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				FilmDetails dto = new FilmDetails();
				dto.setId(rs.getInt("id"));
			    dto.setTitoloFilm(rs.getString("titolo_film"));
			    dto.setGenere(rs.getString("genere"));
			    dto.setRegista(rs.getString("regista"));
			    dto.setAnno(rs.getInt("anno"));
			    dto.setDurataMinuti(rs.getInt("durata_minuti"));
			    dto.setEtaMinima(rs.getInt("eta_minima"));
				return dto;
			} );
					   
			return new GetFilmsResponse(films);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Recupera i dettagli del singolo film a partire dal suo identificatore univoco.
	 *
	 * @param req la richiesta {@link GetFilmsById} contenente l'ID del film
	 * @return l'oggetto {@link GetFilmResponse} contenente i dettagli del film trovato, oppure {@code null} in caso di errore SQL
	 */
	private Response find(GetFilmsById req) {		

		String baseQuery = "SELECT * FROM public.\"Films\"";

		SqlQueryBuilder sqb = new SqlQueryBuilder(baseQuery);

		sqb.and("id = ?", req.getIdFilm());
	
		try {

			List<FilmDetails> films = DbHelper.executeQuery(_connection, sqb.getSql(), sqb.getParams(), rs -> {
				FilmDetails dto = new FilmDetails();
				dto.setId(rs.getInt("id"));
			    dto.setTitoloFilm(rs.getString("titolo_film"));
			    dto.setGenere(rs.getString("genere"));
			    dto.setRegista(rs.getString("regista"));
			    dto.setAnno(rs.getInt("anno"));
			    dto.setDurataMinuti(rs.getInt("durata_minuti"));
			    dto.setEtaMinima(rs.getInt("eta_minima"));
				return dto;
			} );
					   
			return new GetFilmResponse(films.getFirst());

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Operazione non supportata: il catalogo dei film è accessibile esclusivamente in modalità di lettura.
	 *
	 * @param req il comando di modifica inviato
	 * @return non ritorna alcun valore
	 * @throws UnsupportedOperationException sempre, in quanto la modifica/inserimento film non è consentita
	 */
	@Override
	public Response execute(Command req) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Non è possibile inserire un film!");
	}	
}